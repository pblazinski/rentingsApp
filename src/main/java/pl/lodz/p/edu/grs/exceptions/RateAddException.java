package pl.lodz.p.edu.grs.exceptions;

public class RateAddException extends RuntimeException {

    public RateAddException() {
    }

    public RateAddException(final String message) {
        super(message);
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
