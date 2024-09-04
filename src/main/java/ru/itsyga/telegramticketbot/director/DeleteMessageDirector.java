package ru.itsyga.telegramticketbot.director;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessages;

import java.util.Set;

@Component
public class DeleteMessageDirector {
    public DeleteMessages build(Long chatId, Set<Integer> messageIds) {
        return DeleteMessages.builder()
                .chatId(chatId)
                .messageIds(messageIds)
                .build();
    }
}
