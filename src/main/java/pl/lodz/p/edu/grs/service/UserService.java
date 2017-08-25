package pl.lodz.p.edu.grs.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.User;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User registerUser(RegisterUserDTO registerUserDTO);

    void remove(long userId);

    User updatePassword(long userId, String password);

    User updateNames(long userId, String firstName, String lastName);

    User updateEmail(long userId, String email);
}
