package com.willyoubackend.domain.dating.entity;

import lombok.Getter;

@Getter
public enum DatingThemeEnum {
    ROMANTIC("Romantic"),
    CASUAL("Casual"),
    ADVENTUROUS("Adventurous"),
    OUTDOORS("Outdoors"),
    COFFEE_DATE("Coffee Date"),
    MOVIE_NIGHT("Movie Night"),
    FINE_DINING("Fine Dining"),
    SPORTS_FANS("Sports Fans"),
    CREATIVE("Creative"),
    BOOK_LOVERS("Book Lovers"),
    MUSIC_LOVERS("Music Lovers");

    private final String themeName;

    DatingThemeEnum(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return this.themeName;
    }
}
