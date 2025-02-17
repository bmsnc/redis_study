package com.bmsnc.adapter.out.persistence.repository;

import com.bmsnc.adapter.out.persistence.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select distinct s " +
            "from Schedule s " +
            "inner join fetch s.movie m " +
            "inner join fetch s.theater t " +
            "where 1=1 " +
            "and t.theaterId = :theaterId " +
            "and :now between date(s.screenOpenAt) and date(s.screenCloseAt) " +
            "order by m.movieReleaseAt desc, s.movieStartAt asc ")
    List<Schedule> getRunningMovies(@Param("theaterId") Long theaterId, @Param("now") LocalDate now);
}
