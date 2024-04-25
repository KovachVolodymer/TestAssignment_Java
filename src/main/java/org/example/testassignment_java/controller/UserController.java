package org.example.testassignment_java.controller;

import org.example.testassignment_java.model.User;
import org.example.testassignment_java.payload.response.MessageResponse;
import org.example.testassignment_java.repository.UserRepository;
import org.example.testassignment_java.security.jwt.JwtUtil;
import org.example.testassignment_java.services.user.UserDetailsImpl;
import org.example.testassignment_java.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PatchMapping
    public ResponseEntity<Object> patchUser(@RequestBody User user,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.patchUser(user, userDetails.getId());
    }

    @PutMapping
    public ResponseEntity<Object> putUser(@RequestBody User user, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.putUser(user, userDetails.getId());
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(userDetails.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUserAdmin(@RequestBody User user,@PathVariable String id) {

        return userService.patchUser(user, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> putUserAdmin(@RequestBody User user, @PathVariable String id) {
        return userService.putUser(user, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserAdmin(@PathVariable String id){
        return userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/role/{id}")
    public ResponseEntity<Object> addRole(@RequestBody User user, @PathVariable String id) {
        return userService.addRole(user, id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Object> filterByDate(
            @RequestParam(name = "to", defaultValue = "2024-04-20") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
            @RequestParam(name = "from", defaultValue = "2024-04-20") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from) {

        return ResponseEntity.ok(userRepository.findByBirthDateBetween(from, to));
    }

}
