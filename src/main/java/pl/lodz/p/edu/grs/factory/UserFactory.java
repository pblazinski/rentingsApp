package pl.lodz.p.edu.grs.factory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.user.User;

import java.util.HashSet;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(final RegisterUserDto registerUserDto) {
        return User.builder()
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .roles(new HashSet<>())
                .build();
    }
}
