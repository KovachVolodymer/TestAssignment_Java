package org.example.testassignment_java.services.user;


import org.example.testassignment_java.model.User;
import org.springframework.http.ResponseEntity;


import java.util.Date;

public interface UserService {
    ResponseEntity<Object> patchUser(User user, String id);
    ResponseEntity<Object> deleteUser(String id);
    ResponseEntity<Object> putUser(User user, String id);
    ResponseEntity<Object> addRole(User user, String id);
}
