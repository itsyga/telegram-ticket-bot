package ru.itsyga.telegramticketbot.service.chatmessageupdate;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

public interface ChatMessageUpdater {
    void update(Long chatId, BotApiObject apiObject);
}
