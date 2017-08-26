package pl.lodz.p.edu.grs.controller.game;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class UpdateGamePriceDto {

    @Min(0)
    @NotNull
    private double price;
}
