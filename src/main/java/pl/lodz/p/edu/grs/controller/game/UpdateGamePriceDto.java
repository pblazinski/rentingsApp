package pl.lodz.p.edu.grs.controller.game;


import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateGamePriceDto {

    @Min(0)
    @NotNull
    private double price;
}
