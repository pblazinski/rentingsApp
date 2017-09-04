package pl.lodz.p.edu.grs.controller.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import pl.lodz.p.edu.grs.model.user.UserConstants;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_FIRST_NAME)
    private String firstName;

    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_LAST_NAME)
    private String lastName;

    @NotBlank
    @Size(min = UserConstants.MIN_SIZE_PASSWORD)
    private String password;
}
