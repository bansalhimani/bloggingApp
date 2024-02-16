package com.example.blogrestapi.controller;

import com.example.blogrestapi.payload.JwtAuthResponse;
import com.example.blogrestapi.payload.LoginDto;
import com.example.blogrestapi.payload.RegisterDto;
import com.example.blogrestapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> logim(@RequestBody LoginDto loginDto){
        String token=authService.login(loginDto);
        JwtAuthResponse jwtAuthResponse=new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }
}
