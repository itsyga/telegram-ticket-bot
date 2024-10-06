package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chat")
public class Chat implements Persistable<Long> {
    @Id
    @Column(name = "chat_id")
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Request request;

    @ManyToOne
    @JoinColumn(name = "state_id", referencedColumnName = "state_id")
    private State state;

    public Chat(Long id, State state) {
        this.id = id;
        this.state = state;
    }

    @Override
    public boolean isNew() {
        return request == null;
    }
}
