package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;

import java.util.Arrays;
import java.util.HashSet;

public class UserUtil {

    public static final Long USER_ID = 1L;

    public static final String EMAIL = "correct@email.com";

    public static final String FIRST_NAME = "first";

    public static final String LAST_NAME = "last";

    public static final String PASSWORD = "password";

    public static final Long USER_ID2 = 2L;

    public static final String EMAIL2 = "correct2@email.com";

    public static final String FIRST_NAME2 = "first";

    public static final String LAST_NAME2 = "last";

    public static final String PASSWORD2 = "password";


    private UserUtil() {

    }

    public static RegisterUserDto mockRegisterUserDTO() {
        return new RegisterUserDto(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);
    }

    public static User mockUser() {
        return new User(USER_ID,
                EMAIL,
                PASSWORD,
                FIRST_NAME,
                LAST_NAME,
                null,
                new HashSet<>(Arrays.asList(Role.USER))
        );
    }
}


