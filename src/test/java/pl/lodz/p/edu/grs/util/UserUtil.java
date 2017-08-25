package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.User;

public class UserUtil {

    public static final Long USER_ID = 1L;

    public static final String EMAIL = "correct@email.com";

    public static final String FIRST_NAME = "first";

    public static final String LAST_NAME = "last";

    public static final String PASSWORD = "password";

    private UserUtil() {

    }

    public static RegisterUserDTO mockRegisterUserDTO() {
        return new RegisterUserDTO(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
    }

    public static User mockUser() {
        return new User(USER_ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, null);
    }

}
