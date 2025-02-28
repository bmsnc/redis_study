package com.bmsnc.adapter.out.init;

import com.bmsnc.adapter.out.persistence.entity.Movie;
import com.bmsnc.adapter.out.persistence.entity.Schedule;
import com.bmsnc.adapter.out.persistence.entity.Theater;
import com.bmsnc.adapter.out.persistence.repository.MovieRepository;
import com.bmsnc.adapter.out.persistence.repository.ScheduleRepository;
import com.bmsnc.adapter.out.persistence.repository.TheaterRepository;
import com.bmsnc.common.dto.MovieGenre;
import com.bmsnc.common.dto.MovieGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DummyInitializer implements ApplicationRunner {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<Movie> totalMovies = movieRepository.findAll();
        List<Theater> totalTheaters = theaterRepository.findAll();

        LocalDateTime movieStartAt = LocalDateTime.now();
        LocalDateTime screenOpenAt = LocalDateTime.now();
        LocalDateTime screenCloseAt = LocalDateTime.now();

        List<Schedule> dummyScheduleList = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
            dummyScheduleList.add(
                    Schedule.builder()
                            .movie(totalMovies.get((int) ((Math.random() * totalMovies.size()))))
                            .theater(totalTheaters.get((int) ((Math.random() * totalTheaters.size()))))
                            .movieStartAt(movieStartAt
                                            .plusDays((int) (Math.random() * 7) - 3)
                                            .plusHours((int) (Math.random() * 49) - 24))
                            .screenOpenAt(screenOpenAt.minusMonths(1))
                            .screenCloseAt(screenCloseAt.plusYears(1))
                            .build()
            );
        }
        scheduleRepository.saveAllAndFlush(dummyScheduleList);
        
        List<Movie> dummyMovieList = new ArrayList<>();
        StringBuilder sb;
        Random random = new Random();
        List<String> list;
        for (int j = 0; j < 0; j++) {
            sb = new StringBuilder();
            list = new ArrayList<>();
            for(int i=0; i<20; i++) {
                int n = random.nextInt(36);
                if(n>25) list.add(String.valueOf(n-25));
                else list.add(String.valueOf((char)(n+65)));
            }
            for (String s : list) {
                sb.append(s);
            }
            MovieGrade[] grades = MovieGrade.values();
            MovieGenre[] genres = MovieGenre.values();

            dummyMovieList.add(
                    Movie.builder()
                            .movieName(sb.toString())
                            .movieGrade(grades[random.nextInt(grades.length)])
                            .movieReleaseAt(screenOpenAt.minusYears(50))
                            .movieImageUrl("X")
                            .runningTimeMinutes((long) (Math.random() * 150) + 1)
                            .movieGenre(genres[random.nextInt(genres.length)])
                            .build()
            );
        }
        movieRepository.saveAllAndFlush(dummyMovieList);
    }
}
