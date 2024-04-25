package org.example.testassignment_java.controller;

import jakarta.validation.Valid;
import org.example.testassignment_java.model.User;
import org.example.testassignment_java.payload.request.LoginRequest;
import org.example.testassignment_java.payload.request.SignupRequest;
import org.example.testassignment_java.payload.response.MessageResponse;
import org.example.testassignment_java.repository.UserRepository;
import org.example.testassignment_java.security.jwt.JwtUtil;
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

    @Value("${minAge}")
    private Integer minAge;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest signup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обробка помилок валідації
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new MessageResponse(String.join(", ", errorMessages)));
        }

        if (userRepository.existsByEmail(signup.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        LocalDate birthDate = signup.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (birthDate.plusYears(minAge).isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User is under 18 years old!"));
        }

        User user = new User(signup.getEmail(),signup.getFirstName(),signup.getLastName(),
                jwtUtil.encryptPassword(signup.getPassword()),signup.getBirthDate());
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User is registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> logIn(@Valid @RequestBody LoginRequest login, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new MessageResponse(String.join(", ", errorMessages)));
        }

        User user = userRepository.findByEmail(login.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        if (!jwtUtil.checkPassword(login.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid password!"));
        }

        String token = jwtUtil.generateToken(user);
        Map<Object, String> responseMap = new HashMap<>();
        responseMap.put("name", user.getFirstName());
        responseMap.put("email", user.getEmail());
        responseMap.put("token", token);

        return ResponseEntity.ok(responseMap);
    }



}
