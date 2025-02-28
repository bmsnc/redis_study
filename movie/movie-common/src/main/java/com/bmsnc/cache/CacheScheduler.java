package com.bmsnc.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    // 매일 자정(00:00:00)에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "movie", allEntries = true)
    public void clearMovieCache() {
    }
}
