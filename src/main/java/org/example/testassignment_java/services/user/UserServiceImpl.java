package org.example.testassignment_java.services.user;

import org.example.testassignment_java.model.User;
import org.example.testassignment_java.payload.response.MessageResponse;
import org.example.testassignment_java.repository.UserRepository;
import org.example.testassignment_java.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<Object> patchUser(User user, String id) {
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

    @Override
    public ResponseEntity<Object> putUser(User user, String id) {
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

    @Override
    public ResponseEntity<Object> addRole(User user, String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));

        Set<String> roles = userOptional.get().getRoles();
        roles.addAll(user.getRoles());
        userOptional.get().setRoles(roles);
        userRepository.save(userOptional.get());
        return ResponseEntity.ok().body(new MessageResponse("Add new role"));
    }

    @Override
    public ResponseEntity<Object> deleteUser(String id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
            return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }



}
