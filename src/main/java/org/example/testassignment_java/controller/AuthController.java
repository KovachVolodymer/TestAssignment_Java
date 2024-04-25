package org.example.testassignment_java.controller;

import jakarta.validation.Valid;
import org.example.testassignment_java.payload.request.LoginRequest;
import org.example.testassignment_java.payload.request.SignupRequest;
import org.example.testassignment_java.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
