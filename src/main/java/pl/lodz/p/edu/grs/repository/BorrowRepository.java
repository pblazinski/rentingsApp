package pl.lodz.p.edu.grs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.edu.grs.model.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}
