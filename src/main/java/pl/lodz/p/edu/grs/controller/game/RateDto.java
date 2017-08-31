package pl.lodz.p.edu.grs.controller.game;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RateDto {

    private long borrowId;
    @Min(1)
    @Max(10)
    private long rate;
    @Size(max = 255)
    private String comment;

}
