package com.bmsnc.adapter.out.init;

import com.bmsnc.adapter.out.persistence.entity.Movie;
import com.bmsnc.adapter.out.persistence.entity.Schedule;
import com.bmsnc.adapter.out.persistence.entity.Theater;
import com.bmsnc.adapter.out.persistence.repository.MovieRepository;
import com.bmsnc.adapter.out.persistence.repository.ScheduleRepository;
import com.bmsnc.adapter.out.persistence.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < 1000; i++) {
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
    }
}
