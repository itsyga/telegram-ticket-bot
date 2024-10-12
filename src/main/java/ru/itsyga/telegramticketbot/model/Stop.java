package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Stop {
    @JsonProperty("desc")
    private String name;

    @JsonProperty("info")
    private String description;

    @JsonProperty("important")
    private boolean isDefault;

    @JsonProperty("datetime")
    private LocalDateTime dateTime;

    public String getStopNameWithDescription() {
        return (description != null && !description.isEmpty()) ? name + " (" + description + ")" : name;
    }
}
