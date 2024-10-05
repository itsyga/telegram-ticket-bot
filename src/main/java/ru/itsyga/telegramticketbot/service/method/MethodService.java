package ru.itsyga.telegramticketbot.service.method;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

public interface MethodService {
    void serve(BotApiObject apiObject);
}
