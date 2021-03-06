package pl.lodz.p.edu.grs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Page<Borrow> findBorrowsByUserEmail(Pageable pageable, String email);

    Optional<Borrow> findByUserEmailAndBorrowedGamesIdIn(String email, long gameId);

    List<Borrow> findBorrowsByTimeBack(LocalDateTime time);

    List<Borrow> findAllByBorrowedGamesId(Long gameId);

    List<Borrow> findAllByBorrowedGamesIn(List<Game> games);

}
