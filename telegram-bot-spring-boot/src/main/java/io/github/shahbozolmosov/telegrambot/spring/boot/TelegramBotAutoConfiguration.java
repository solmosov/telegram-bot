package io.github.shahbozolmosov.telegrambot.spring.boot;


import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationProvider;
import io.github.shahbozolmosov.telegrambot.bot.TelegramBotApplication;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TelegramBotApplicationFactory telegramBotApplicationFactory(
            TelegramBotProperties properties,
            ObjectProvider<AuthorizationProvider> authorizationProviders
    ) {
        return new TelegramBotApplicationFactory(properties, authorizationProviders.getIfAvailable());
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
}
