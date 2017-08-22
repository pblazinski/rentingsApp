package pl.lodz.p.edu.grs.exceptions;


public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException() {
    }

    public CategoryInUseException(String message) {
        super(message);
    }

    public CategoryInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryInUseException(Throwable cause) {
        super(cause);
    }

    public CategoryInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
