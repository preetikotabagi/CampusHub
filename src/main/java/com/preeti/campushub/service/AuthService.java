package com.preeti.campushub.service;

import com.preeti.campushub.dto.auth.AuthenticationResponse;
import com.preeti.campushub.dto.auth.LoginRequest;
import com.preeti.campushub.dto.auth.RegisterRequest;
import com.preeti.campushub.dto.user.ChangePasswordRequest;

public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    void changePassword(ChangePasswordRequest request);
}