package com.ul.SmartDine.dtos.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType="Bearer";
    private long expiresIn;
    private UserResponseDto user;
}
