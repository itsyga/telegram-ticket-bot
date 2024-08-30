package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    @JsonAlias("desc")
    private String name;
}
