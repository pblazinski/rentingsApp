package pl.lodz.p.edu.grs.controller.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UpdateGameAvailabilityDto {

    @NotNull
    private boolean available;
}
