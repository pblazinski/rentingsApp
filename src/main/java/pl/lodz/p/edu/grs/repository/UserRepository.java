package pl.lodz.p.edu.grs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.edu.grs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
