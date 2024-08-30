package ru.itsyga.telegramticketbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itsyga.telegramticketbot.entity.State;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Short> {
    @Query("SELECT s FROM State s JOIN FETCH s.phrase")
    List<State> findStates();
}
