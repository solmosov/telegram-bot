package io.github.solmosov.telegrambot.spring.boot;


import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
class TelegramBotAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TelegramBotApplicationFactory telegramBotApplicationFactory(
            List<TelegramBotRegistration> registrations
    ) {
        return new TelegramBotApplicationFactory(registrations);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramBotApplication telegramBotApplication(
            TelegramBotApplicationFactory factory
    ) {
        return factory.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramBotLifecycle telegramBotLifecycle(
            TelegramBotApplication application
    ) {
        return new TelegramBotLifecycle(application);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBotHandlerRegistrar springBotHandlerRegistrar(
            ListableBeanFactory beanFactory,
            TelegramBotApplication application
    ) {
        return new SpringBotHandlerRegistrar(beanFactory, application);
    }

    @Bean
    public static SpringBotBeanDefinitionRegistrar springBotBeanDefinitionRegistrar(ConfigurableListableBeanFactory beanFactory) {
        return new SpringBotBeanDefinitionRegistrar(beanFactory);
    }
}
