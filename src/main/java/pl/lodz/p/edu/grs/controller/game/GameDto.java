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
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private boolean available;

    @NotNull
    private double price;

    @NotNull
    private long categoryId;
}
