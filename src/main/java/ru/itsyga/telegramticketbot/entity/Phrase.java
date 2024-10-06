package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "phrase")
public class Phrase {
    @Id
    @Column(name = "phrase_id")
    private Short id;

    @Column(name = "phrase_text")
    private String text;
}
