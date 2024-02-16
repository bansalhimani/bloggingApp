package com.example.blogrestapi.service;

import com.example.blogrestapi.payload.LoginDto;
import com.example.blogrestapi.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
