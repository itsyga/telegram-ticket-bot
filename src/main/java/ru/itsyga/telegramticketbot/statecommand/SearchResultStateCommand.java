package ru.itsyga.telegramticketbot.statecommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.entity.Chat;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchResultStateCommand implements StateCommand {
    @Override
    public void execute(Chat chat, String msgText) {

    }

    @Override
    public Set<String> getStateNames() {
        return Set.of("search results");
    }
}
