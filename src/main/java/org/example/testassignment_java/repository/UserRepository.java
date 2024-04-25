package org.example.testassignment_java.repository;

import org.example.testassignment_java.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("{'birthDate' : { $gte: ?0, $lte: ?1 } }")
    List<User> findByBirthDateBetween(Date from, Date to);

}
