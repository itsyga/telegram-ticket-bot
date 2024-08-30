package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "message")
public class ChatMessage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "message_text")
    private String messageText;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public ChatMessage(Integer messageId, String messageText, Chat chat) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.chat = chat;
    }
}
