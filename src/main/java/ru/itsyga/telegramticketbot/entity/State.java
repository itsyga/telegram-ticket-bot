package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "state")
public class State {
    @Id
    @Column(name = "state_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "state_name")
    private String name;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Phrase phrase;
}
