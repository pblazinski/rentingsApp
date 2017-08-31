package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;

import java.util.Arrays;
import java.util.Collections;


public class BorrowUtil {





    public static Borrow mockBorrow() {
        Game game = GameUtil.mockGame();
        return Borrow.builder()
                .borrowedGames(Collections.singletonList(game))
                .penalties(0)
                .totalPrice(game.getPrice())
                .id(1L)
                .user(UserUtil.mockUser())
                .build();
    }


    public static BorrowDto mockBorrowDto() {
        return new BorrowDto(Arrays.asList(1L, 2L));
    }

}
