package com.ul.SmartDine.dtos.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private final Instant timestamp;
    private final int status;
    private final String errorCode;
    private final String message;
    private final Map<String, String> fieldErrors;

    public static ErrorResponseDto of(HttpStatus httpStatus, String errorCode, String message) {
        return ErrorResponseDto.builder().timestamp(Instant.now()).status(httpStatus.value()).errorCode(errorCode).message(message).build();
    }
}