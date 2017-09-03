package pl.lodz.p.edu.grs.exceptions;

import lombok.Getter;

@Getter
public class GameAddRateException extends RuntimeException {

    public GameAddRateException(final String message) {
        super(message);
    }

}
