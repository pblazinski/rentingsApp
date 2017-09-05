package pl.lodz.p.edu.grs.controller.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class UpdateUserEmailDto {

    @Email
    @NotBlank
    private String email;
}
