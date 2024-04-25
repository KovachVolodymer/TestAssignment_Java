package org.example.testassignment_java.services.auth;

import org.example.testassignment_java.payload.request.LoginRequest;
import org.example.testassignment_java.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> register(SignupRequest signup);
    ResponseEntity<Object> logIn(LoginRequest login);
}
