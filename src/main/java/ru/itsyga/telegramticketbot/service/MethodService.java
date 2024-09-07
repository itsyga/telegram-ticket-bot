package ru.itsyga.telegramticketbot.service;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

public interface MethodService {
    void serve(BotApiObject apiObject);

    void updateChatMessages(Long chatId, BotApiObject apiObject);
}
