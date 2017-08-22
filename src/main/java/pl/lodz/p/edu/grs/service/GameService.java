package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.model.Game;

import java.util.List;

public interface GameService {

    Page<Game> findAll(Pageable pageable);

    List<Game> findAll();

    Game addGame(Game game, Long id);

    Game updateGame(Game game, Long id);

    void removeGame(Long id);

    Game getGame(Long id);
}
