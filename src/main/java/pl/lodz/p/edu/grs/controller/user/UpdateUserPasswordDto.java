package pl.lodz.p.edu.grs.controller.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import pl.lodz.p.edu.grs.model.user.UserConstants;

import javax.validation.constraints.Size;

//todo add mising min and test
@Getter
@Setter
public class UpdateUserPasswordDto {

    @NotBlank
    @Size(min = UserConstants.MIN_SIZE_PASSWORD)
    private String password;

}
