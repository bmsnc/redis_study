package com.bmsnc.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
public record SearchRunningMoviesRequest (
    @NotNull
    Long theaterId,

    @Size(max = 255)
    String movieName,
    String movieGenre
) {}
