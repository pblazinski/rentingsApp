package pl.lodz.p.edu.grs.controller.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import pl.lodz.p.edu.grs.model.user.UserConstants;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateUserNamesDto {

    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_FIRST_NAME)
    private String firstName;
    @NotBlank
    @Size(max = UserConstants.MAX_SIZE_LAST_NAME)
    private String lastName;

}
