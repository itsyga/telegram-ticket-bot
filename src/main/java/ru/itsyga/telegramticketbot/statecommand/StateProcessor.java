package ru.itsyga.telegramticketbot.statecommand;

import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.entity.Chat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateProcessor {
    private final Map<String, StateCommand> stateCommandMap;

    public StateProcessor(List<StateCommand> stateCommands) {
        this.stateCommandMap = stateCommands.stream()
                .flatMap(stateCommand -> stateCommand.getStateNames().stream()
                        .map(name -> Map.entry(name, stateCommand)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void distribute(Chat chat, String msgText) {
        String stateName = chat.getState().getName();
        StateCommand stateCommand = stateCommandMap.get(stateName);
        stateCommand.execute(chat, msgText);
    }
}
