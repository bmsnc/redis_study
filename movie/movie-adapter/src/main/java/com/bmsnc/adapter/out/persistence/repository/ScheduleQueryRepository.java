package com.bmsnc.adapter.out.persistence.repository;

import com.bmsnc.adapter.out.querydsl.model.MovieQueryModel;
import com.bmsnc.adapter.out.querydsl.model.QMovieQueryModel;
import com.bmsnc.applicaion.port.in.RunningMovieCommand;

import com.bmsnc.common.dto.MovieGenre;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.bmsnc.adapter.out.persistence.entity.QMovie.movie;
import static com.bmsnc.adapter.out.persistence.entity.QSchedule.schedule;
import static com.bmsnc.adapter.out.persistence.entity.QTheater.theater;


@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public List<MovieQueryModel> searchRunningMovies(RunningMovieCommand command) {
        return jpaQueryFactory
                .select(new QMovieQueryModel(
                        movie.movieId,
                        movie.movieName,
                        movie.movieGrade,
                        movie.movieReleaseAt,
                        movie.movieImageUrl,
                        movie.runningTimeMinutes,
                        movie.movieGenre,
                        theater.theaterName,
                        schedule.movieStartAt
                ))
                .from(schedule)
                .innerJoin(movie)
                    .on(schedule.movie.movieId.eq(movie.movieId))
                .innerJoin(theater)
                    .on(schedule.theater.theaterId.eq(theater.theaterId))
                .where(
                        theater.theaterId.eq(command.theaterId()),
                        isScreening(),
                        likeMovieName(command.movieName()),
                        eqMovieGenre(command.movieGenre())
                )
                .orderBy(movie.movieReleaseAt.asc(), schedule.movieStartAt.asc())
                .fetch();
    }

    BooleanExpression likeMovieName(String movieName) {
        return StringUtils.hasText(movieName) ? movie.movieName.contains(movieName) : null;
    }

    BooleanExpression eqMovieGenre(MovieGenre movieGenre) {
        return !StringUtils.hasText(movieGenre.toString()) ? null
                : (MovieGenre.ALL.equals(movieGenre) ? null
                : movie.movieGenre.eq(movieGenre));

    }

    public BooleanExpression isScreening() {
        LocalDate today = LocalDate.now();
        return Expressions.asDate(today)
                .between(
                        Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, {1})", schedule.screenOpenAt, "%Y-%m-%d"),
                        Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, {1})", schedule.screenCloseAt, "%Y-%m-%d")
                );
    }
}
