package org.example.testassignment_java.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Document(collection = "Users")
public class User {
    @Id
    public String id;

    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String phoneNumber;
    public String address;

    public Date birthDate;

    private Set<String> roles = new HashSet<>();

    public User(String email, String firstName, String lastName, String password,Date birthDate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthDate = birthDate;
        roles.add("ROLE_USER");//default role
    }

    public User() {
    }

}
