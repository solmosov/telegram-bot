package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.annotation.BotHandler;
import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import io.github.solmosov.telegrambot.messaging.TelegramMessaging;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SpringBotBeanDefinitionRegistrarTest {

    @Test
    void shouldInjectMessagingFromDeclaredBot() {
        try (var context = new AnnotationConfigApplicationContext()) {

            TelegramBotApplication application =
                    new TelegramBotApplication();

            application.register("bot1", "token-1");
            application.register("bot2", "token-2");

            context.getBeanFactory().registerSingleton(
                    "telegramBotApplication",
                    application
            );

            context.registerBean(
                    SpringBotBeanDefinitionRegistrar.class,
                    () -> new SpringBotBeanDefinitionRegistrar(
                            context.getBeanFactory()
                    )
            );

            context.registerBean(Bot1Handler.class);
            context.registerBean(Bot2Handler.class);

            context.refresh();

            Bot1Handler bot1Handler = context.getBean(Bot1Handler.class);
            Bot2Handler bot2Handler = context.getBean(Bot2Handler.class);

            assertThat(bot1Handler.messaging)
                    .isSameAs(application.getBot("bot1").messaging());

            assertThat(bot2Handler.messaging)
                    .isSameAs(application.getBot("bot2").messaging());

            assertThat(bot1Handler.messaging)
                    .isNotSameAs(bot2Handler.messaging);
        }
    }

    @BotHandler("bot1")
    @Component
    static class Bot1Handler {

        private final TelegramMessaging messaging;

        Bot1Handler(TelegramMessaging messaging) {
            this.messaging = messaging;
        }
    }

    @BotHandler("bot2")
    @Component
    static class Bot2Handler {

        private final TelegramMessaging messaging;

        Bot2Handler(TelegramMessaging messaging) {
            this.messaging = messaging;
        }
    }

}