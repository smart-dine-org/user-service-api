package com.ul.SmartDine.dtos.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    @NotBlank(message = "First name is required!")
    @Size(max = 100,message = "First name must not exceed 100 chars")
    private String firstName;
    @NotBlank(message = "Last name is required!")
    @Size(max = 100,message = "Last name must not exceed 100 chars")
    private String lastName;
    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be a valid email address")
    private String email;
    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;
    @NotBlank(message = "Password is required!")
    private String password;
    @NotBlank(message = "Confirm password is required!")
    private String confirmPassword;
}
