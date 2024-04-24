package org.example.testassignment_java.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Date;

@Getter
public class SignupRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 3,message = "First name must be at least 3 characters")
    @Size(max = 20,message = "First name must be less than 20 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3,message = "Last name must be at least 3 characters")
    @Size(max = 20,message = "Last name must be less than 20 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 6,message = "Password must be at least 6 characters")
    @Size(max = 40,message = "Password must be less than 40 characters")
    private String password;


    private Date birthDate;


}
