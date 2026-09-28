package com.ul.SmartDine.service;

import com.ul.SmartDine.dtos.req.*;
import com.ul.SmartDine.dtos.resp.AuthResponseDto;
import com.ul.SmartDine.dtos.resp.TokenRefreshResponseDto;

public interface AuthService {
    void signup(SignupRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);
    AuthResponseDto loginWithGoogle(GoogleLoginRequestDto dto);
    AuthResponseDto loginWithGithub(GithubLoginRequestDto dto);
    TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto dto);
    void logout(String refreshToken);
}
