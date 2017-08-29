package pl.lodz.p.edu.grs.util;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.service.UserService;

@Component
public class StubHelper {

    private static UserService userService;

    public StubHelper(final UserService userService) {
        StubHelper.userService = userService;
    }


    public static User stubUser() {
        RegisterUserDto userDTO = UserUtil.mockRegisterUserDTO();
        return userService.registerUser(userDTO);
    }

}
