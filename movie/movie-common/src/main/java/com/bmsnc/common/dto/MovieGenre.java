package com.bmsnc.common.dto;

public enum MovieGenre {
    ALL,
    COMEDY,
    FANTASY,
    ROMANCE,
    ACTION,
    ANIMATION,
    HORROR;

    public static boolean anyMatch(String genre) {
        for (MovieGenre movieGenre : MovieGenre.values()) {
            if (movieGenre.toString().equals(genre)) {
                return true;
            }
        }
        return false;
    }
}

