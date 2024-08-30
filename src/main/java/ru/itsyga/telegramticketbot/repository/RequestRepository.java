package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itsyga.telegramticketbot.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
