package ru.itsyga.telegramticketbot.controller.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultHandler implements UpdateHandler {
    @Override
    public void handleUpdate(Update update) {

    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
    }
}
