package com.ul.SmartDine.service;

import com.ul.SmartDine.dtos.req.UpdateProfileRequestDto;
import com.ul.SmartDine.dtos.resp.UserResponseDto;

import java.util.UUID;

public interface UserService {
    UserResponseDto getProfile(UUID userId);
    UserResponseDto updateProfile(UUID userId, UpdateProfileRequestDto dto);
    void deleteAccount(UUID userId);
}
