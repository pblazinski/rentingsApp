package pl.lodz.p.edu.grs.factory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.user.User;

import java.util.HashSet;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(final RegisterUserDTO registerUserDTO) {
        return User.builder()
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .email(registerUserDTO.getEmail())
                .password(passwordEncoder.encode(registerUserDTO.getPassword()))
                .roles(new HashSet<>())
                .build();
    }
}
