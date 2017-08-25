package pl.lodz.p.edu.grs.factory;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;

@Component
public class GameFactory {

    public Game create(final GameDto gameDto, Category category) {
        return Game.builder()
                .title(gameDto.getTitle())
                .description(gameDto.getDescription())
                .available(gameDto.isAvailable())
                .price(gameDto.getPrice())
                .category(category)
                .build();
    }
}
