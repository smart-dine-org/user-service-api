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
public class GithubLoginRequestDto {
    @NotBlank(message = "Github Authentication Code is required!")
    private String code;
}
