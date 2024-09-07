package ru.itsyga.telegramticketbot.service;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.itsyga.telegramticketbot.entity.Chat;

public interface MethodService {
    void serve(BotApiObject apiObject);

    void updateChatMessages(Chat chat, BotApiObject apiObject);
}
