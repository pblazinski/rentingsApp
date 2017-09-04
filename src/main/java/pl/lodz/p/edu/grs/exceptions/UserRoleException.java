package pl.lodz.p.edu.grs.exceptions;

public class UserRoleException extends RuntimeException {

    public UserRoleException() {
    }

    public UserRoleException(final String message) {
        super(message);
    }

    public UserRoleException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserRoleException(final Throwable cause) {
        super(cause);
    }

    public UserRoleException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
