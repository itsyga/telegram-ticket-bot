package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itsyga.telegramticketbot.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findChatMessagesByChat_Id(Long chatId);

    void deleteChatMessagesByChat_Id(Long chatId);
}
