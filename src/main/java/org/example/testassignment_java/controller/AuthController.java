package org.example.testassignment_java.controller;

import jakarta.validation.Valid;
import org.example.testassignment_java.model.User;
import org.example.testassignment_java.payload.request.LoginRequest;
import org.example.testassignment_java.payload.request.SignupRequest;
import org.example.testassignment_java.payload.response.MessageResponse;
import org.example.testassignment_java.repository.UserRepository;
import org.example.testassignment_java.security.jwt.JwtUtil;
import org.example.testassignment_java.security.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest signup) {
        return authService.register(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> logIn(@Valid @RequestBody LoginRequest login)
    {
        return authService.logIn(login);
    }



}
