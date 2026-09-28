package com.ul.SmartDine.service;

import com.ul.SmartDine.dtos.req.ForgotPasswordRequestDto;
import com.ul.SmartDine.dtos.req.ResetPasswordRequestDto;

public interface PasswordService {
    void forgotPassword(ForgotPasswordRequestDto dto);
    void resetPassword(ResetPasswordRequestDto dto);
}
