package ru.itsyga.telegramticketbot.service.method;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.statecommand.StateProcessor;
import ru.itsyga.telegramticketbot.textcommand.TextCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService implements MethodService {
    private final StateProcessor stateProcessor;
    private final RepositoryService repositoryService;
    private final Map<String, TextCommand> textCommandMap;

    public MessageService(RepositoryService repositoryService, List<TextCommand> textCommands, StateProcessor stateProcessor) {
        this.repositoryService = repositoryService;
        this.textCommandMap = textCommands.stream()
                .collect(Collectors.toMap(TextCommand::getCommandText, textCommand -> textCommand));
        this.stateProcessor = stateProcessor;
    }

    @Override
    public void serve(BotApiObject apiObject) {
        Message message = (Message) apiObject;
        Long chatId = message.getChatId();
        String msgText = message.getText();
        Chat chat = repositoryService.findOrCreateChat(chatId);

        TextCommand textCommand;
        if ((textCommand = textCommandMap.get(msgText)) != null) {
            textCommand.execute(chat);
            return;
        }

        stateProcessor.distribute(chat, msgText);
    }
}
