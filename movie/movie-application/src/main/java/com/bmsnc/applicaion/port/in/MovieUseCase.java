package com.bmsnc.applicaion.port.in;



import com.bmsnc.applicaion.domain.model.MovieModel;

import java.util.List;

public interface MovieUseCase {

    List<MovieModel> getRunningMovies(RunningMovieCommand command);
    List<MovieModel> searchRunningMovies(RunningMovieCommand command);

}
