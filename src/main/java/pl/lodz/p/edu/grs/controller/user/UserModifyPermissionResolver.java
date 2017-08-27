package pl.lodz.p.edu.grs.controller.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;

import java.util.Optional;

@Component
public class UserModifyPermissionResolver {

    private final UserRepository userRepository;

    public UserModifyPermissionResolver(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean hasAuthorityToModifyUser(final AppUser userDetails, final long userId) {
        Optional<User> optional = userRepository.findByEmail(userDetails.getUsername());

        if (!optional.isPresent()) {
            return false;
        }

        User user = optional.get();

        return userDetails.hasAuthority(Authority.MODIFY_USER) && user.getId() == userId;
    }
}
