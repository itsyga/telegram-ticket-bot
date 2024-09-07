package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itsyga.telegramticketbot.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Modifying
    @Query("DELETE FROM Chat ch WHERE ch.id = :chat_id")
    void deleteChat(@Param("chat_id") Long chatId);
}
