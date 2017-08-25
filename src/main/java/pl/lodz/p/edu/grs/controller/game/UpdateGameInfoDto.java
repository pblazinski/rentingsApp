package pl.lodz.p.edu.grs.controller.game;

import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
public class UpdateGameInfoDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
