package pl.lodz.p.edu.grs.controller.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

//todo add mising min and test
@Getter
@Setter
public class UpdateUserPasswordDto {

    @NotBlank
    private String password;
}
