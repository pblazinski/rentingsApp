package pl.lodz.p.edu.grs.controller.borrow;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;

import java.util.Optional;

@Component
public class BorrowPermissionResolver {

    private final UserRepository userRepository;

    public BorrowPermissionResolver(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean hasAuthorityToGetBorrowsList(final AppUser userDetails) {
        Optional<User> optional = userRepository.findByEmail(userDetails.getUsername());

        if (!optional.isPresent()) {
            return false;
        }

        return userDetails.hasAuthority(Authority.LIST_BORROWS);
    }
}
