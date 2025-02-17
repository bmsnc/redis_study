package com.bmsnc.common;

import lombok.Builder;
import java.util.List;

@Builder
public record Result<T> (
    String message,
    int status,
    T data
) {
    public static <T> Result<T> of(String message, int status, T data) {
        if ((data == null || (data instanceof List<?> list && list.isEmpty())) && (message == null || message.isEmpty())) {
            return new Result<>("조회된 결과가 없습니다.", 200, null);
        }
        return new Result<>(message, status, data);
    }
}


