package com.example.blogrestapi.service.implementation;

import com.example.blogrestapi.exception.BlogApiException;
import com.example.blogrestapi.model.Role;
import com.example.blogrestapi.model.User;
import com.example.blogrestapi.payload.LoginDto;
import com.example.blogrestapi.payload.RegisterDto;
import com.example.blogrestapi.repository.RoleRepository;
import com.example.blogrestapi.repository.UserRepository;
import com.example.blogrestapi.security.JwtTokenProvider;
import com.example.blogrestapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,UserRepository userRepository,
                           RoleRepository roleRepository,PasswordEncoder passwordEncoder,JwtTokenProvider jwtTokenProvider){
        this.authenticationManager=authenticationManager;
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtTokenProvider=jwtTokenProvider;
    }
    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginDto.getUsernameOrEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Username already exists");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Email already exists");
        }

        User user=new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles=new HashSet<>();
        Role role=roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully";

    }
}
