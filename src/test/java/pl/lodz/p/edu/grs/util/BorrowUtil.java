package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;

import java.util.ArrayList;
import java.util.Arrays;


public class BorrowUtil {

    public static final long BORROW_ID = 1L;

    public static Borrow mockBorrow() {
        Game game = GameUtil.mockGame();
        return Borrow.builder()
                .borrowedGames(new ArrayList<>(Arrays.asList(game)))
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
