package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.dtos.req.UpdateProfileRequestDto;
import com.ul.SmartDine.dtos.resp.UserResponseDto;
import com.ul.SmartDine.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public UserResponseDto getProfile(UUID userId) {
        return null;
    }

    @Override
    public UserResponseDto updateProfile(UUID userId, UpdateProfileRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto deleteAccount(UUID userId) {
        return null;
    }
}
