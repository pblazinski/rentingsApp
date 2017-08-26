package pl.lodz.p.edu.grs.controller.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UpdateGameInfoDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
