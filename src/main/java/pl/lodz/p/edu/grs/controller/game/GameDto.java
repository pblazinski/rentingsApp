package pl.lodz.p.edu.grs.controller.game;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
public class GameDto {

    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotNull
    boolean available;

    @NotNull
    double price;
}
