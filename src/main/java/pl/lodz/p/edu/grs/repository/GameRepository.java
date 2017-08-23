package pl.lodz.p.edu.grs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.edu.grs.model.Game;

import java.util.List;

@Repository
public interface GameRepository  extends JpaRepository<Game, Long> {

    List<Game> findByCategoryId(Long id);
}
