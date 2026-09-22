package com.ul.SmartDine.dtos.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpResponseDto {
    private boolean success;
    private String message;
    private String email;
}
