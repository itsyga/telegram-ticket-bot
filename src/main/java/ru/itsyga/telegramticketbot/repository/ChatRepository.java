package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itsyga.telegramticketbot.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
