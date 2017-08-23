package pl.lodz.p.edu.grs.controller.user;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UpdateUserEmailDto {

    @Email
    @NotBlank
    private String email;
}
