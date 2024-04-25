package org.example.testassignment_java.security.services.auth;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService{

    @Value("${minAge}")
    private Integer minAge;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public ResponseEntity<Object> register(SignupRequest signup) {
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

    @Override
    public ResponseEntity<Object> logIn(LoginRequest login) {
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
