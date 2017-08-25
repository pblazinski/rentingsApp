package pl.lodz.p.edu.grs.controller.game;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateGameAvailabilityDto {

    @NotNull
    private boolean available;
}
