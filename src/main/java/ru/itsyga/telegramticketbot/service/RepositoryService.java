package ru.itsyga.telegramticketbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.entity.State;
import ru.itsyga.telegramticketbot.repository.ChatMessageRepository;
import ru.itsyga.telegramticketbot.repository.ChatRepository;
import ru.itsyga.telegramticketbot.repository.RequestRepository;
import ru.itsyga.telegramticketbot.repository.StateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final ChatRepository chatRepository;
    private final StateRepository stateRepository;
    private final RequestRepository requestRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Chat findOrCreateChat(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseGet(() -> {
                    State firstState = stateRepository.findStates().getFirst();
                    Chat chat = chatRepository.save(new Chat(chatId, firstState));
                    requestRepository.save(new Request(chatId));
                    return chat;
                });
    }

    public List<ChatMessage> findChatMessages(Long chatId) {
        return chatMessageRepository.findChatMessagesByChat_Id(chatId);
    }

    public void deleteChatMessages(Long chatId) {
        chatMessageRepository.deleteChatMessagesByChat_Id(chatId);
    }
}
