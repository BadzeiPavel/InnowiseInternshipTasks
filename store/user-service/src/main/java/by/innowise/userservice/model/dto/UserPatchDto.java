package by.innowise.userservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserPatchDto {
    @Size(min = 2, max = 50, message = "Name must be from 2 to 50 characters")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be from 2 to 50 characters")
    private String surname;

    @Past
    private LocalDate birthDate;

    @Email
    @Size(min = 5, max = 100, message = "Email must be from 5 to 100 characters")
    private String email;
}
