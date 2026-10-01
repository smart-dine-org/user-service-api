package com.ul.SmartDine.service.impl;

import com.ul.SmartDine.dtos.req.*;
import com.ul.SmartDine.dtos.resp.AuthResponseDto;
import com.ul.SmartDine.dtos.resp.TokenRefreshResponseDto;
import com.ul.SmartDine.dtos.resp.UserResponseDto;
import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.entity.enums.AuthProvider;
import com.ul.SmartDine.entity.enums.UserStatus;
import com.ul.SmartDine.exceptions.AccountSuspendedException;
import com.ul.SmartDine.exceptions.DuplicateEmailException;
import com.ul.SmartDine.exceptions.EmailNotVerifiedException;
import com.ul.SmartDine.exceptions.PasswordMismatchException;
import com.ul.SmartDine.repository.UserRepository;
import com.ul.SmartDine.service.AuthService;
import com.ul.SmartDine.service.OtpService;
import com.ul.SmartDine.util.KeycloakUtil;
import com.ul.SmartDine.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final KeycloakUtil keyCloakUtil;
    private final OtpService otpService;
    private final UserMapper userMapper;

    @Override
    public void signup(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException(dto.getEmail());
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
        String keycloakId = keyCloakUtil.createUser(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        User user = User.builder().keycloakId(keycloakId).firstName(dto.getFirstName()).lastName(dto.getLastName()).email(dto.getEmail()).phoneNumber(dto.getPhoneNumber()).status(UserStatus.PENDING_VERIFICATION).provider(AuthProvider.LOCAL).emailVerified(false).build();
        userRepository.save(user);
        otpService.sendOtp(dto.getEmail());
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            throw new EmailNotVerifiedException(dto.getEmail());
        }
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new AccountSuspendedException(dto.getEmail());
        }
        Map<String, Object> tokenResponse = keyCloakUtil.authenticateUser(dto.getEmail(), dto.getPassword());
        return buildAuthResponse(tokenResponse, user);
    }

    @Override
    public AuthResponseDto loginWithGoogle(GoogleLoginRequestDto dto) {
        Map<String, Object> googleUser = keyCloakUtil.exchangeGoogleToken(dto.getIdToken());
        String email = (String) googleUser.get("email");
        String firstName = (String) googleUser.get("given_name");
        String lastName = (String) googleUser.get("family_name");
        String pictureUrl = (String) googleUser.get("picture");
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String keycloakId = keyCloakUtil.createSocialUser(email, firstName, lastName, AuthProvider.GOOGLE);
            User newUser = User.builder().keycloakId(keycloakId).firstName(firstName).lastName(lastName).email(email).profilePictureUrl(pictureUrl).status(UserStatus.ACTIVE).provider(AuthProvider.GOOGLE).emailVerified(true).build();
            return userRepository.save(newUser);
        });
        Map<String, Object> tokenResponse = keyCloakUtil.impersonateUser(user.getKeycloakId());
        return buildAuthResponse(tokenResponse, user);
    }

    @Override
    public AuthResponseDto loginWithGitHub(GitHubLoginRequestDto dto) {
        Map<String, Object> githubUser = keyCloakUtil.exchangeGitHubCode(dto.getCode());
        String email = (String) githubUser.get("email");
        String name = (String) githubUser.get("name");
        String avatarUrl = (String) githubUser.get("avatar_url");
        String firstName = name != null && name.contains(" ") ? name.split(" ")[0] : name;
        String lastName = name != null && name.contains(" ") ? name.substring(name.indexOf(" ") + 1) : "";
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String keycloakId = keyCloakUtil.createSocialUser(email, firstName, lastName, AuthProvider.GITHUB);
            User newUser = User.builder().keycloakId(keycloakId).firstName(firstName).lastName(lastName).email(email).profilePictureUrl(avatarUrl).status(UserStatus.ACTIVE).provider(AuthProvider.GITHUB).emailVerified(true).build();
            return userRepository.save(newUser);
        });
        Map<String, Object> tokenResponse = keyCloakUtil.impersonateUser(user.getKeycloakId());
        return buildAuthResponse(tokenResponse, user);
    }

    @Override
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto dto) {
        Map<String, Object> tokenResponse = keyCloakUtil.refreshToken(dto.getRefreshToken());
        return TokenRefreshResponseDto.builder().accessToken((String) tokenResponse.get("access_token")).refreshToken((String) tokenResponse.get("refresh_token")).expiresIn(((Number) tokenResponse.get("expires_in")).longValue()).build();
    }

    @Override
    public void logout(String refreshToken) {
        keyCloakUtil.logout(refreshToken);
    }

    private AuthResponseDto buildAuthResponse(Map<String, Object> tokenResponse, User user) {
        UserResponseDto userDto = userMapper.toDto(user);
        return AuthResponseDto.builder().accessToken((String) tokenResponse.get("access_token")).refreshToken((String) tokenResponse.get("refresh_token")).expiresIn(((Number) tokenResponse.get("expires_in")).longValue()).user(userDto).build();
    }
}
