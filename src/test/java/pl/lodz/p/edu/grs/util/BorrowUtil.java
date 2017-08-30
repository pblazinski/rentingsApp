package pl.lodz.p.edu.grs.util;

import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
