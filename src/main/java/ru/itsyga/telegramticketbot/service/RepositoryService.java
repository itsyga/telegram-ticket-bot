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
import ru.itsyga.telegramticketbot.util.StateAction;

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
                    State firstState = getFirstState();
                    Chat chat = chatRepository.save(new Chat(chatId, firstState));
                    requestRepository.save(new Request(chatId));
                    return chat;
                });
    }

    @Transactional
    public Chat updateChatState(Chat chat, StateAction stateAction) {
        List<State> states = stateRepository.findStates();
        int currentIndex = states.indexOf(chat.getState());
        switch (stateAction) {
            case NEXT_STATE -> chat.setState(states.get(currentIndex + 1));
            case PREVIOUS_STATE -> chat.setState(states.get(currentIndex - 1));
            case RESET_STATE -> chat.setState(states.getFirst());
        }
        return chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(Long chatId) {
        chatRepository.deleteChat(chatId);
    }

    public void saveChatMessages(List<ChatMessage> chatMessages) {
        chatMessageRepository.saveAll(chatMessages);
    }

    public List<ChatMessage> findChatMessages(Long chatId) {
        return chatMessageRepository.findChatMessagesByChat_Id(chatId);
    }

    @Transactional
    public void deleteChatMessages(Long chatId) {
        chatMessageRepository.deleteChatMessages(chatId);
    }

    public void updateRequest(Request request) {
        requestRepository.save(request);
    }

    private State getFirstState() {
        return stateRepository.findStates().getFirst();
    }
}
