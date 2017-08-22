package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.factory.UserFactory;
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserFactory userFactory;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository,
                           final UserFactory userFactory,
                           final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> findAll(final Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        log.info("Found <{}> users", users.getTotalElements());

        return users;
    }

    @Override
    public User registerUser(final RegisterUserDTO registerUserDTO) {
        User user = userFactory.createUser(registerUserDTO);

        user = userRepository.save(user);

        log.info("Register user <{}> with email <{}>", user.getId(), user.getEmail());

        return user;
    }

    @Override
    public void remove(final long userId) {
        User user = userRepository.getOne(userId);

        userRepository.delete(user);

        log.info("Removed user with id <{}>", user.getId());
    }

    @Override
    public User updatePassword(final long userId, final String password) {
        User user = userRepository.getOne(userId);

        String newPassword = passwordEncoder.encode(password);

        user.updatePassword(newPassword);

        user = userRepository.save(user);

        log.info("Updated password for user with id <{}>", userId);

        return user;
    }

    @Override
    public User updateNames(final long userId,
                            final String firstName,
                            final String lastName) {
        User user = userRepository.getOne(userId);

        user.updateNames(firstName, lastName);

        user = userRepository.save(user);

        log.info("Updated user names from <{} {}> to <{} {}> for user with id <{}>",
                firstName,
                lastName,
                userId
        );

        return user;
    }

    @Override
    public User updateEmail(final long userId,
                            final String email) {

        User user = userRepository.getOne(userId);

        user.updateEmail(email);

        user = userRepository.save(user);

        log.info("Updated user email from <{}> to <{}> for user with id <{}>",
                email,
                userId
        );

        return user;
    }

}
