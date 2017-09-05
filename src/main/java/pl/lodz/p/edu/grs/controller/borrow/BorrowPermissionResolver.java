package pl.lodz.p.edu.grs.controller.borrow;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.SystemAdminPermissionResolver;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;

import java.util.Objects;
import java.util.Optional;

@Component
public class BorrowPermissionResolver {

    private final UserRepository userRepository;
    private final SystemAdminPermissionResolver systemAdminPermissionResolver;
    private final BorrowRepository borrowRepository;

    public BorrowPermissionResolver(final UserRepository userRepository,
                                    final SystemAdminPermissionResolver systemAdminPermissionResolver,
                                    final BorrowRepository borrowRepository) {
        this.userRepository = userRepository;
        this.systemAdminPermissionResolver = systemAdminPermissionResolver;
        this.borrowRepository = borrowRepository;
    }

    public boolean hasAuthoritiyToGetBorrow(final AppUser userDetails, final long borrowId) {
        Optional<User> optional = userRepository.findByEmail(userDetails.getUsername());

        if (!optional.isPresent()) {
            return false;
        }

        if (!borrowRepository.exists(borrowId)) {
            return false;
        }

        Borrow borrow = borrowRepository.findOne(borrowId);

        User user = optional.get();

        return systemAdminPermissionResolver.isSystemAdmin(user) ||
                (userDetails.hasAuthority(Authority.GET_BORROW) && Objects.equals(user.getId(), borrow.getUser().getId()));
    }
}
