package com.bmsnc.adapter.out.persistence.repository;

import com.bmsnc.adapter.out.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
