package com.bmsnc.applicaion.domain.service;


import com.bmsnc.applicaion.domain.model.MovieModel;
import com.bmsnc.applicaion.port.in.MovieUseCase;
import com.bmsnc.applicaion.port.in.RunningMovieCommand;
import com.bmsnc.applicaion.port.out.RunningMoviesPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieUseCaseService implements MovieUseCase {

    private final RunningMoviesPort runningMoviesPort;

    @Override
    public List<MovieModel> getRunningMovies(RunningMovieCommand command) {
        return runningMoviesPort.getRunningMovies(command);
    }

    @Override
    public List<MovieModel> searchRunningMovies(@Valid RunningMovieCommand command) {
        return runningMoviesPort.searchRunningMovies(command);
    }
}
