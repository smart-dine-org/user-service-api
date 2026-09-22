package com.ul.SmartDine.dtos.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {
    private boolean success;
    private String message;
    private int status;
    private Map<String, String> errors;
    @Builder.Default
    private LocalDateTime timestamps = LocalDateTime.now();
}
