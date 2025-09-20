package by.innowise.user_service.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserPatchDto {
    @Size(min = 2, max = 50)
    private String name;

    @Size(min = 2, max = 50)
    private String surname;

    @Past
    private LocalDateTime birthDate;

    @Email
    @Size(min = 5, max = 100)
    private String email;
}
