package ru.itsyga.telegramticketbot.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramTicketBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
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

    }
}
