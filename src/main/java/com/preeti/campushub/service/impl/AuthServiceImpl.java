package com.preeti.campushub.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.auth.AuthenticationResponse;
import com.preeti.campushub.dto.auth.LoginRequest;
import com.preeti.campushub.dto.auth.RegisterRequest;
import com.preeti.campushub.dto.user.ChangePasswordRequest;
import com.preeti.campushub.entity.User;
import com.preeti.campushub.exception.auth.UserAlreadyExistsException;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.repository.UserRepository;
import com.preeti.campushub.security.jwt.JwtService;
import com.preeti.campushub.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        User user = User.builder()
            .fullName(request.getFullName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .phoneNumber(request.getPhoneNumber())
            .role(request.getRole())
            .passwordChanged(true)
            .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .role(user.getRole().name())
            .passwordChanged(user.getPasswordChanged())
            .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .role(user.getRole().name())
            .passwordChanged(user.getPasswordChanged())
            .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        user.setPasswordChanged(true);

        userRepository.save(user);
    }
}