package com.ul.SmartDine.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshRequestDto {
    @NotBlank(message = "Refresh token is required!")
    private String refreshToken;
}
