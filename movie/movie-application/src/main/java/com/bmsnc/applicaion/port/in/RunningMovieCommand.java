package com.bmsnc.applicaion.port.in;

import com.bmsnc.common.dto.MovieGenre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
public record RunningMovieCommand (
    @NotNull
    Long theaterId,
    @Size(max = 255)
    String movieName,
    MovieGenre movieGenre
) {}
