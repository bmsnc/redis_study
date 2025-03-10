package com.bmsnc.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    Movie("movie", 24);

    private final String cacheName;
    private final int expiredAfterWrite;
}
