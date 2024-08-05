package ru.itsyga.telegramticketbot.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.itsyga.telegramticketbot.controller.handler.DefaultHandler;
import ru.itsyga.telegramticketbot.controller.handler.UpdateHandler;

import java.util.List;

@Component
public class TelegramTicketBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final UpdateHandler chainHead;

    public TelegramTicketBot(List<UpdateHandler> handlers) {
        if (handlers.isEmpty()) {
            chainHead = new DefaultHandler();
        } else {
            chainHead = handlers.getFirst();
            for (int i = 0; i < handlers.size(); i++) {
                var current = handlers.get(i);
                var next = i < handlers.size() - 1 ? handlers.get(i + 1) : new DefaultHandler();
                current.setNext(next);
            }
        }
    }

    @Override
    public String getBotToken() {
        return System.getenv("bot_token");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        chainHead.handleUpdate(update);
    }
}
