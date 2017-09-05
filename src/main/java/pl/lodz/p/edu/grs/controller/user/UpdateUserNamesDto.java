package pl.lodz.p.edu.grs.controller.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
//todo add missing max and test
@Getter
@Setter
public class UpdateUserNamesDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

}
