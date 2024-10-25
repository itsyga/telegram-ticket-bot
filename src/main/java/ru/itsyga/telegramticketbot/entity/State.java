package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "state")
public class State {
    @Id
    @Column(name = "state_id")
    private Short id;

    @Column(name = "state_name", length = 30)
    private String name;

    @OneToOne
    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Exclude
    private Phrase phrase;
}
