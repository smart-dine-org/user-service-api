package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.dtos.req.*;
import com.ul.SmartDine.dtos.resp.AuthResponseDto;
import com.ul.SmartDine.dtos.resp.TokenRefreshResponseDto;
import com.ul.SmartDine.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public void signup(SignupRequestDto dto) {

    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        return null;
    }

    @Override
    public AuthResponseDto loginWithGoogle(GoogleLoginRequestDto dto) {
        return null;
    }

    @Override
    public AuthResponseDto loginWithGithub(GithubLoginRequestDto dto) {
        return null;
    }

    @Override
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto dto) {
        return null;
    }

    @Override
    public void logout(String refreshToken) {

    }
}
