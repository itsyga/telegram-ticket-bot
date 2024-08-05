package ru.itsyga.telegramticketbot.controller.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handleUpdate(Update update);

    void setNext(UpdateHandler nextHandler);
}
