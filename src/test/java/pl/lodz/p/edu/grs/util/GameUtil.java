package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Game;

import java.time.LocalDateTime;


public class GameUtil {

    public static final Long GAME_ID = 1L;

    public static final String TITLE = "Title";

    public static final String DESCRIPTION = "Description";

    public static final boolean AVAILABLE = true;

    public static final LocalDateTime TIME = LocalDateTime.of(1, 1, 1, 1, 1);

    public static final double PRICE = 20.99;

    private GameUtil() {
    }

    public static GameDto mockGameDto() {
        return new GameDto(TITLE, DESCRIPTION, AVAILABLE, PRICE);
    }

    public static Game mockGame() {
        return new Game(GAME_ID, TITLE, DESCRIPTION, AVAILABLE, TIME, PRICE, CategoryUtil.mockCategory());
    }
}
