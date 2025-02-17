package com.bmsnc.applicaion.domain.model;

import com.bmsnc.common.dto.MovieGenre;
import com.bmsnc.common.dto.MovieGrade;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record MovieModel (
    Long movieId,
    String movieName,
    MovieGrade movieGrade,
    LocalDateTime movieReleaseAt,
    String movieImageUrl,
    Long runningTimeMinutes,
    MovieGenre movieGenre,
    String theaterName,
    LocalDateTime movieStartAt
) {}
