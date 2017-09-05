package pl.lodz.p.edu.grs.exceptions;

import lombok.Getter;
import pl.lodz.p.edu.grs.model.game.Rate;

@Getter
public class RateAddException extends RuntimeException {

    private Rate rate;

    public RateAddException(final String message, final Rate rate) {
        super(message);
        this.rate = rate;
    }

}
