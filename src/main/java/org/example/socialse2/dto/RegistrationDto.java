package org.example.socialse2.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.socialse2.validator.PasswordMatches;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@PasswordMatches(message = "Passwords do not match")
public class RegistrationDto {

    private String firstName;

    private String lastName;

    private String email;

    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String confirmPassword;

}
