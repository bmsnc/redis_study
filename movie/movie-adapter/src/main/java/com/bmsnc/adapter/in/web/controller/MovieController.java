package com.bmsnc.adapter.in.web.controller;

import com.bmsnc.adapter.in.web.request.SearchRunningMoviesRequest;
import com.bmsnc.applicaion.domain.model.MovieModel;
import com.bmsnc.applicaion.domain.service.MovieUseCaseService;
import com.bmsnc.applicaion.port.in.RunningMovieCommand;
import com.bmsnc.common.Result;
import com.bmsnc.common.dto.MovieGenre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    /**
     * 필요한 필드만 조회 하기 위해 QueryDsl 사용한 API
     */
    @GetMapping("/searchRunningMovies")
    public Result<List<MovieModel>> searchRunningMovies(@Valid SearchRunningMoviesRequest request) {
        Random random = new Random();
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<10; i++) {
            int n = random.nextInt(36);
            if(n>25) list.add(String.valueOf(n-25));
            else list.add(String.valueOf((char)(n+65)));
        }
        for (String s : list) {
            sb.append(s);
        }
        MovieGenre movieGenre =  MovieGenre.anyMatch(request.movieGenre()) ? MovieGenre.valueOf(request.movieGenre()) : MovieGenre.ALL;
        RunningMovieCommand command = RunningMovieCommand.builder()
//                .theaterId(request.theaterId())
                .theaterId((long) (Math.random() * 100000) + 1)
//                .movieName(request.movieName())
                .movieName(sb.toString())
                .movieGenre(movieGenre)
                .build();

        return Result.of("Success", 200, movieUseCaseService.searchRunningMovies(command));
    }
}
