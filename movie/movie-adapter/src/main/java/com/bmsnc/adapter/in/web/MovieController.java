package com.bmsnc.adapter.in.web;

import com.bmsnc.adapter.out.persistence.entity.Movie;
import com.bmsnc.applicaion.domain.model.MovieModel;
import com.bmsnc.applicaion.domain.service.MovieUseCaseService;
import com.bmsnc.applicaion.port.in.RunningMovieCommand;
import com.bmsnc.common.Result;
import com.bmsnc.common.dto.MovieGenre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieUseCaseService movieUseCaseService;

    @GetMapping("/running/{theaterId}")
    public Result<List<MovieModel>> getRunningMovies(@PathVariable Long theaterId) {
        RunningMovieCommand command = RunningMovieCommand.builder()
                .theaterId(theaterId)
                .build();

        return Result.of("Success", 200, movieUseCaseService.getRunningMovies(command));
    }

    // QueryDsl
    @GetMapping("/searchRunningMovies")
    public Result<List<MovieModel>> searchRunningMovies(@Valid SearchRunningMoviesRequest request) {
        MovieGenre movieGenre =  MovieGenre.anyMatch(request.movieGenre()) ? MovieGenre.valueOf(request.movieGenre()) : MovieGenre.ALL;
        RunningMovieCommand command = RunningMovieCommand.builder()
                .theaterId(request.theaterId())
                .movieName(request.movieName())
                .movieGenre(movieGenre)
                .build();

        return Result.of("Success", 200, movieUseCaseService.searchRunningMovies(command));
    }
}
