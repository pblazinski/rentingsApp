package pl.lodz.p.edu.grs.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.UserUtil;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BorrowFactoryTest {

    private BorrowFactory borrowFactory;

    @Before
    public void setUp() throws Exception {
        borrowFactory = new BorrowFactory();
    }

    @Test
    public void shouldCreateBorrowObject() throws Exception {
        //given
        Game game = GameUtil.mockGame();
        User user = UserUtil.mockUser();

        //when
        Borrow borrow = borrowFactory.create(Collections.singletonList(game), user);

        //then
        assertThat(borrow.getBorrowedGames().size())
                .isEqualTo(1);
        assertThat(borrow.getBorrowedGames().get(0).getId())
                .isEqualTo(game.getId());
        assertThat(borrow.getUser().getId())
                .isEqualTo(user.getId());
    }

    @Test
    public void shouldCreateTwoDifferentObjecct() throws Exception {
        //given
        Game game = GameUtil.mockGame();
        User user = UserUtil.mockUser();

        Borrow borrow = borrowFactory.create(Collections.singletonList(game), user);

        //then
        Borrow result = borrowFactory.create(Collections.singletonList(game), user);

        //then
        assertThat(borrow)
                .isNotSameAs(result);
    }
}
