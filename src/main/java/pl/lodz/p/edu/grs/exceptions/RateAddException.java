package pl.lodz.p.edu.grs.exceptions;

import lombok.Getter;
import pl.lodz.p.edu.grs.model.game.Rate;

@Getter
public class RateAddException extends RuntimeException {

    private Rate rate;

    public RateAddException() {
    }

    public RateAddException(final String message, final Rate rate) {
        super(message);
        this.rate = rate;
    }

    public RateAddException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RateAddException(final Throwable cause) {
        super(cause);
    }

    public RateAddException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
