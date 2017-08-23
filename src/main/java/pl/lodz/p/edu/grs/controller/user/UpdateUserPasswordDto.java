package pl.lodz.p.edu.grs.controller.user;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UpdateUserPasswordDto {

    @NotBlank
    private String password;
}
