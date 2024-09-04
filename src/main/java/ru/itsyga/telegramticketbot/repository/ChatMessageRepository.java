package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itsyga.telegramticketbot.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findChatMessagesByChat_Id(Long chatId);

    @Modifying
    @Query("DELETE FROM ChatMessage m WHERE m.chat.id = :chat_id")
    void deleteChatMessages(@Param("chat_id") Long chatId);
}
