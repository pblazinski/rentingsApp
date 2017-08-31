package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Game;

import java.util.List;

public interface GameService {

    Page<Game> findAll(Pageable pageable);

    List<Game> getMostPopular(Long amount);

    Game addGame(GameDto gameDto);

    Game updateTitleAndDescription(Long id, String title, String description);

    Game updatePrice(Long id, double price);

    Game updateAvailability(Long id, boolean available);

    Game updateCategory(Long id, Long categoryId);

    void remove(Long id);

    Game getGame(Long id);
}
