package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "message")
public class ChatMessage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "message_text", length = 30)
    private String messageText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public ChatMessage(Integer messageId, String messageText, Chat chat) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.chat = chat;
    }
}
