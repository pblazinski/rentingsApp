package pl.lodz.p.edu.grs.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.model.User;

import java.util.List;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    List<User> findAll();

    User addUser(User user);
}
