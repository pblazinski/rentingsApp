package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.model.Game;

public interface GameService {

    Page<Game> findAll(Pageable pageable);

    Game addGame(Game game);
}
