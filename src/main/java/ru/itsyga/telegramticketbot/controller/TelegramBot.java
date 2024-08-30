package ru.itsyga.telegramticketbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.itsyga.telegramticketbot.controller.handler.DefaultHandler;
import ru.itsyga.telegramticketbot.controller.handler.UpdateHandler;

import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @Value("${bot.token}")
    private String botToken;
    private final UpdateHandler chainHead;

    public TelegramBot(List<UpdateHandler> handlers) {
        if (handlers.isEmpty()) {
            this.chainHead = new DefaultHandler();
        } else {
            this.chainHead = handlers.getFirst();
            int size = handlers.size();
            for (int i = 0; i < size; i++) {
                var current = handlers.get(i);
                var next = i < (size - 1) ? handlers.get(i + 1) : new DefaultHandler();
                current.setNext(next);
            }
        }
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        this.chainHead.handleUpdate(update);
    }
}
