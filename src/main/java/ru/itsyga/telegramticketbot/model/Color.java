package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Color {
    @JsonProperty("name")
    private String name;
}
