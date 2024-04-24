package org.example.testassignment_java.repository;

import org.example.testassignment_java.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
}
