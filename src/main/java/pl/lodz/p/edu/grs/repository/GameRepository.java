package pl.lodz.p.edu.grs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.edu.grs.model.Game;

/**
 * Created by pblazinski on 24.03.17.
 */
@Repository
public interface GameRepository  extends JpaRepository<Game, Long> {
}
