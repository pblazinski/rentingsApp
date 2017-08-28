package pl.lodz.p.edu.grs.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.user.User;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User findOne(long userId);

    User registerUser(RegisterUserDTO registerUserDTO);

    User createSystemAdmin(RegisterUserDTO registerUserDTO);

    void remove(long userId);

    User updatePassword(long userId, String password);

    User updateNames(long userId, String firstName, String lastName);

    User updateEmail(long userId, String email);

    User findByEmail(String email);
}
