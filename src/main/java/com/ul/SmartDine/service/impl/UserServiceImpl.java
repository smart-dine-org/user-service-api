package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.dtos.req.UpdateProfileRequestDto;
import com.ul.SmartDine.dtos.resp.UserResponseDto;
import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.repository.UserRepository;
import com.ul.SmartDine.service.UserService;
import com.ul.SmartDine.util.KeycloakUtil;
import com.ul.SmartDine.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakUtil keycloakUtil;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateProfile(UUID userId, UpdateProfileRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        keycloakUtil.updateUserProfile(user.getKeycloakId(), dto.getFirstName(),dto.getLastName());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteAccount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        keycloakUtil.deleteUser(user.getKeycloakId());
        userRepository.delete(user);
    }
}
