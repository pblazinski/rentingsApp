package pl.lodz.p.edu.grs.controller;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;

@Component
public class SystemAdminPermissionResolver {

    public boolean isSystemAdmin(final User user) {
        return user.getRoles().contains(Role.SYSTEM_ADMIN);
    }
}
