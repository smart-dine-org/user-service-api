package com.ul.SmartDine.util.mapper;

import com.ul.SmartDine.dtos.resp.UserResponseDto;
import com.ul.SmartDine.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponseDto toDto(User user) {
        return UserResponseDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).phoneNumber(user.getPhoneNumber()).profilePictureUrl(user.getProfilePictureUrl()).status(user.getStatus()).provider(user.getProvider()).emailVerified(user.isEmailVerified()).phoneVerified(user.isPhoneVerified()).twoFactorEnabled(user.isTwoFactorEnabled()).roles(user.getRoles() != null ? user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()) : java.util.Set.of()).build();
    }
}
