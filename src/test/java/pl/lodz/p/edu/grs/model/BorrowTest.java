package pl.lodz.p.edu.grs.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BorrowTest {

    private Borrow borrow;

    private static final double TOTAL_PRICE = 10.12;
    private static final double PENALTIES = 12.191;
    private static final LocalDateTime TIME_BACK = LocalDateTime.now();
    private static final User USER = new User();
    private static final List<Game> BORROWED_GAMES = Arrays.asList(new Game());
    private static final LocalDateTime DEADLINE = LocalDateTime.now();

    @Before
    public void setUp() {
        borrow = new Borrow();
    }

    @Test
    public void shouldUpdateTotalPrice() {
        //given

        //when
        borrow.updateTotalPrice(TOTAL_PRICE);

        //then
        assertThat(borrow.getTotalPrice())
                .isNotNull()
                .isEqualTo(TOTAL_PRICE);
    }

    @Test
    public void shouldReplaceTotalPrice() {
        //given
        final double totalPrice = 19.99;
        borrow.updateTotalPrice(totalPrice);

        //when
        borrow.updateTotalPrice(TOTAL_PRICE);

        //then
        assertThat(borrow.getTotalPrice())
                .isNotNull()
                .isNotSameAs(totalPrice)
                .isEqualTo(TOTAL_PRICE);
    }

    @Test
    public void shouldUpdatePenalties() {
        //given

        //when
        borrow.updatePenalties(PENALTIES);

        //then
        assertThat(borrow.getPenalties())
                .isNotNull()
                .isEqualTo(PENALTIES);
    }

    @Test
    public void shouldReplacePenalties() {
        //given
        final double penalties = 129.01;
        borrow.updatePenalties(penalties);

        //when
        borrow.updatePenalties(PENALTIES);

        //then
        assertThat(borrow.getPenalties())
                .isNotNull()
                .isNotSameAs(penalties)
                .isEqualTo(PENALTIES);
    }

    @Test
    public void shouldUpdateTimeBack() {
        //given

        //when
        borrow.updateTimeBack(TIME_BACK);

        //then
        assertThat(borrow.getTimeBack())
                .isNotNull()
                .isEqualTo(TIME_BACK);
    }

    @Test
    public void shouldReplaceTimeBack() {
        //given
        final LocalDateTime timeBack = LocalDateTime.now().minusHours(1);
        borrow.updateTimeBack(timeBack);

        //when
        borrow.updateTimeBack(TIME_BACK);

        //then
        assertThat(borrow.getTimeBack())
                .isNotNull()
                .isNotSameAs(timeBack)
                .isEqualTo(TIME_BACK);
    }

    @Test
    public void shouldUpdateUser() {
        //given

        //when
        borrow.updateUser(USER);

        //then
        assertThat(borrow.getUser())
                .isNotNull()
                .isSameAs(USER);
    }

    @Test
    public void shouldReplaceUser() {
        //given
        final User user = new User();
        borrow.updateUser(user);

        //when
        borrow.updateUser(USER);

        //then
        assertThat(borrow.getUser())
                .isNotNull()
                .isNotSameAs(user)
                .isSameAs(USER);
    }

    @Test
    public void shouldUpdateBorrowedGames() {
        //given

        //when
        borrow.updateBorrowedGames(BORROWED_GAMES);

        //then
        assertThat(borrow.getBorrowedGames())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(BORROWED_GAMES);
    }

    @Test
    public void shouldReplaceBorrowedGames() {
        //given
        final List<Game> borrowedGames = Arrays.asList(new Game());
        borrow.updateBorrowedGames(borrowedGames);

        //when
        borrow.updateBorrowedGames(BORROWED_GAMES);

        //then
        assertThat(borrow.getBorrowedGames())
                .isNotNull()
                .isNotEmpty()
                .isNotSameAs(borrowedGames)
                .isEqualTo(BORROWED_GAMES);
    }

    @Test
    public void shouldUpdateDeadline() {
        //given

        //when
        borrow.updateDeadline(DEADLINE);

        //then
        assertThat(borrow.getDeadline())
                .isNotNull()
                .isEqualTo(DEADLINE);
    }

    @Test
    public void shouldReplaceDeadline() {
        //given
        final LocalDateTime deadline = LocalDateTime.now().minusHours(1);
        borrow.updateDeadline(deadline);

        //when
        borrow.updateDeadline(DEADLINE);

        //then
        assertThat(borrow.getDeadline())
                .isNotNull()
                .isNotSameAs(deadline)
                .isEqualTo(DEADLINE);
    }

}