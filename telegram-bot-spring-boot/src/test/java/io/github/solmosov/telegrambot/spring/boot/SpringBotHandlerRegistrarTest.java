package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.annotation.BotHandler;
import io.github.solmosov.telegrambot.bot.TelegramBot;
import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.Map;

import static org.mockito.Mockito.*;

class SpringBotHandlerRegistrarTest {

    @Test
    void shouldRegisterAllBotHandlers() {
        ListableBeanFactory beanFactory = mock(ListableBeanFactory.class);
        TelegramBotApplication application = mock(TelegramBotApplication.class);
        TelegramBot bot = mock(TelegramBot.class);

        TestHandler firstHandler = new TestHandler();
        AnotherHandler secondHandler = new AnotherHandler();

        when(beanFactory.getBeansWithAnnotation(BotHandler.class))
                .thenReturn(Map.of(
                        "firstHandler", firstHandler,
                        "secondHandler", secondHandler
                ));

        when(application.getBot("my-bot"))
                .thenReturn(bot);

        SpringBotHandlerRegistrar registrar =
                new SpringBotHandlerRegistrar(beanFactory, application);

        registrar.afterSingletonsInstantiated();

        verify(application, times(2)).getBot("my-bot");
        verify(bot).registerHandler(firstHandler);
        verify(bot).registerHandler(secondHandler);
    }

    @Test
    void shouldDoNothingWhenThereAreNoHandlers() {
        ListableBeanFactory beanFactory = mock(ListableBeanFactory.class);
        TelegramBotApplication application = mock(TelegramBotApplication.class);

        when(beanFactory.getBeansWithAnnotation(BotHandler.class))
                .thenReturn(Map.of());

        SpringBotHandlerRegistrar registrar =
                new SpringBotHandlerRegistrar(beanFactory, application);

        registrar.afterSingletonsInstantiated();

        verifyNoInteractions(application);
    }

    @Test
    void shouldRegisterHandlerToCorrectBot() {
        ListableBeanFactory beanFactory = mock(ListableBeanFactory.class);
        TelegramBotApplication application = mock(TelegramBotApplication.class);

        TelegramBot firstBot = mock(TelegramBot.class);
        TelegramBot secondBot = mock(TelegramBot.class);

        FirstBotHandler firstHandler = new FirstBotHandler();
        SecondBotHandler secondHandler = new SecondBotHandler();

        when(beanFactory.getBeansWithAnnotation(BotHandler.class))
                .thenReturn(Map.of(
                        "first", firstHandler,
                        "second", secondHandler
                ));

        when(application.getBot("first-bot"))
                .thenReturn(firstBot);

        when(application.getBot("second-bot"))
                .thenReturn(secondBot);

        SpringBotHandlerRegistrar registrar =
                new SpringBotHandlerRegistrar(beanFactory, application);

        registrar.afterSingletonsInstantiated();

        verify(application).getBot("first-bot");
        verify(application).getBot("second-bot");

        verify(firstBot).registerHandler(firstHandler);
        verify(secondBot).registerHandler(secondHandler);

        verifyNoMoreInteractions(firstBot, secondBot);
    }

    @BotHandler("my-bot")
    static class TestHandler {
    }

    @BotHandler("my-bot")
    static class AnotherHandler {
    }

    @BotHandler("first-bot")
    static class FirstBotHandler {
    }

    @BotHandler("second-bot")
    static class SecondBotHandler {
    }
}