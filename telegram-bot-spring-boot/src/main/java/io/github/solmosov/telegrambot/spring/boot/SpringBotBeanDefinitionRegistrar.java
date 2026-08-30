package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.annotation.BotHandler;
import io.github.solmosov.telegrambot.bot.TelegramBot;
import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.messaging.TelegramMessaging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.reflect.Constructor;

public class SpringBotBeanDefinitionRegistrar implements BeanDefinitionRegistryPostProcessor {

    private final ConfigurableListableBeanFactory beanFactory;


    SpringBotBeanDefinitionRegistrar(
            ConfigurableListableBeanFactory beanFactory
    ) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {

            AbstractBeanDefinition definition = (AbstractBeanDefinition) registry.getBeanDefinition(beanName);

            if (definition.getBeanClassName() == null) {
                continue;
            }

            Class<?> beanClass;

            try {
                beanClass = Class.forName(definition.getBeanClassName());
            } catch (ClassNotFoundException ex) {
                continue;
            }

            BotHandler annotation = beanClass.getAnnotation(BotHandler.class);

            if (annotation == null) {
                continue;
            }

            String botName = annotation.value();

            definition.setInstanceSupplier(() ->
                    create(beanClass, botName)
            );
        }
    }

    private Object create(Class<?> beanClass, String botName) {
        TelegramBotApplication application = beanFactory.getBean(TelegramBotApplication.class);

        TelegramBot bot = application.getBot(botName);

        Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

        Object[] arguments = new Object[constructor.getParameterCount()];

        for (int i = 0; i < constructor.getParameterCount(); i++) {
            Class<?> paramType = constructor.getParameterTypes()[i];

            arguments[i] = resolve(paramType, bot);
        }

        try {
            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Failed to create Telegram bot handler: " + beanClass.getName(), ex
            );
        }
    }

    private Object resolve(Class<?> paramType, TelegramBot bot) {
        if (paramType == TelegramBot.class) {
            return bot;
        }

        if (paramType == TelegramClient.class) {
            return bot.client();
        }

        if (paramType == TelegramMessaging.class) {
            return bot.messaging();
        }

        return beanFactory.getBean(paramType);
    }
}
