package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TelegramBotAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(TelegramBotAutoConfiguration.class);

    @Test
    void shouldCreateRequiredBeans() {
        contextRunner
                .withBean(
                        TelegramBotLifecycle.class,
                        () -> mock(TelegramBotLifecycle.class)
                )
                .run(context -> {
                    assertNotNull(
                            context.getBean(TelegramBotApplicationFactory.class)
                    );
                    assertNotNull(
                            context.getBean(TelegramBotApplication.class)
                    );
                    assertNotNull(
                            context.getBean(TelegramBotLifecycle.class)
                    );
                    assertNotNull(
                            context.getBean(SpringBotHandlerRegistrar.class)
                    );
                });
    }

    @Test
    void shouldCreateApplicationWithRegistrations() {
        TelegramBotRegistration registration =
                TelegramBotRegistration.builder()
                        .botName("test-bot")
                        .token("test-token")
                        .build();

        contextRunner
                .withBean(
                        TelegramBotLifecycle.class,
                        () -> mock(TelegramBotLifecycle.class)
                )
                .withBean(
                        TelegramBotRegistration.class,
                        () -> registration
                )
                .run(context -> {
                    TelegramBotApplication application =
                            context.getBean(TelegramBotApplication.class);

                    assertTrue(application.containsBot("test-bot"));
                    assertEquals(1, application.bots().size());
                });
    }

    @Test
    void shouldBackOffWhenApplicationFactoryAlreadyExists() {
        TelegramBotApplicationFactory factory =
                new TelegramBotApplicationFactory(java.util.List.of());

        contextRunner
                .withBean(TelegramBotApplicationFactory.class, () -> factory)
                .withBean(
                        TelegramBotLifecycle.class,
                        () -> mock(TelegramBotLifecycle.class)
                )
                .run(context -> {
                    assertSame(
                            factory,
                            context.getBean(TelegramBotApplicationFactory.class)
                    );
                });
    }

    @Test
    void shouldBackOffWhenTelegramBotApplicationAlreadyExists() {
        TelegramBotApplication application =
                new TelegramBotApplication();

        contextRunner
                .withBean(
                        TelegramBotApplication.class,
                        () -> application
                )
                .withBean(
                        TelegramBotLifecycle.class,
                        () -> mock(TelegramBotLifecycle.class)
                )
                .run(context -> {
                    assertSame(
                            application,
                            context.getBean(TelegramBotApplication.class)
                    );
                });
    }

    @Test
    void shouldBackOffWhenLifecycleAlreadyExists() {
        TelegramBotLifecycle lifecycle = mock(TelegramBotLifecycle.class);

        contextRunner
                .withBean(TelegramBotLifecycle.class, () -> lifecycle)
                .run(context -> {
                    assertSame(
                            lifecycle,
                            context.getBean(TelegramBotLifecycle.class)
                    );
                });
    }

    @Test
    void shouldBackOffWhenHandlerRegistrarAlreadyExists() {
        TelegramBotApplication application =
                new TelegramBotApplication();

        ListableBeanFactory beanFactory =
                mock(ListableBeanFactory.class);

        SpringBotHandlerRegistrar registrar =
                new SpringBotHandlerRegistrar(
                        beanFactory,
                        application
                );

        contextRunner
                .withBean(
                        SpringBotHandlerRegistrar.class,
                        () -> registrar
                )
                .withBean(
                        TelegramBotLifecycle.class,
                        () -> mock(TelegramBotLifecycle.class)
                )
                .run(context -> {
                    assertSame(
                            registrar,
                            context.getBean(
                                    SpringBotHandlerRegistrar.class
                            )
                    );
                });
    }
}