package org.example.testassignment_java.controller;

import org.example.testassignment_java.model.User;
import org.example.testassignment_java.payload.response.MessageResponse;
import org.example.testassignment_java.repository.UserRepository;
import org.example.testassignment_java.security.jwt.JwtUtil;
import org.example.testassignment_java.security.services.UserDetailsImpl;
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
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @PatchMapping
    public ResponseEntity<Object> patchUser(@RequestBody User user,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        Optional<User> userToUpdate = userRepository.findById(userDetails.getId());

        if (userToUpdate.isPresent()) {
            User u = userToUpdate.get();
            if (userRepository.existsByEmail(u.getEmail()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));

            Optional.ofNullable(user.getEmail()).ifPresent(u::setEmail);
            Optional.ofNullable(user.getFirstName()).ifPresent(u::setFirstName);
            Optional.ofNullable(user.getLastName()).ifPresent(u::setLastName);
            Optional.ofNullable(user.getPassword()).ifPresent(p -> u.setPassword(jwtUtil.encryptPassword(p)));
            Optional.ofNullable(user.getBirthDate()).ifPresent(u::setBirthDate);

            userRepository.save(u);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @PutMapping
    public ResponseEntity<Object> putUser(@RequestBody User user, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<User> userToUpdate = userRepository.findById(userDetails.getId());

        if (userToUpdate.isPresent()) {
            User u = userToUpdate.get();
            if (userRepository.existsByEmail(u.getEmail()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));

            u.setEmail(user.getEmail());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setPassword(jwtUtil.encryptPassword(user.getPassword()));
            u.setBirthDate(user.getBirthDate());

            userRepository.save(u);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<User> userToDelete = userRepository.findById(userDetails.getId());

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
            return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUserAdmin(@RequestBody User user,@PathVariable String id) {

        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            User u = userToUpdate.get();
            if (userRepository.existsByEmail(u.getEmail()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));

            Optional.ofNullable(user.getEmail()).ifPresent(u::setEmail);
            Optional.ofNullable(user.getFirstName()).ifPresent(u::setFirstName);
            Optional.ofNullable(user.getLastName()).ifPresent(u::setLastName);
            Optional.ofNullable(user.getPassword()).ifPresent(p -> u.setPassword(jwtUtil.encryptPassword(p)));
            Optional.ofNullable(user.getBirthDate()).ifPresent(u::setBirthDate);

            userRepository.save(u);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> putUserAdmin(@RequestBody User user, @PathVariable String id) {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            User u = userToUpdate.get();
            if (userRepository.existsByEmail(u.getEmail()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));

            u.setEmail(user.getEmail());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setPassword(jwtUtil.encryptPassword(user.getPassword()));
            u.setBirthDate(user.getBirthDate());

            userRepository.save(u);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserAdmin(@PathVariable String id){
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
            return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/role/{id}")
    public ResponseEntity<Object> patchRole(@RequestBody User user, @PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));

        Set<String> roles = userOptional.get().getRoles();
        roles.addAll(user.getRoles());
        userOptional.get().setRoles(roles);
        userRepository.save(userOptional.get());
        return ResponseEntity.ok().body(new MessageResponse("Add new role"));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Object> filterByDate(
            @RequestParam(name = "to", defaultValue = "2024-04-20") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
            @RequestParam(name = "from", defaultValue = "2024-04-20") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from) {

        return ResponseEntity.ok(userRepository.findByBirthDateBetween(from, to));
    }

}
