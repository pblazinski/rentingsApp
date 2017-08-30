package pl.lodz.p.edu.grs.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.util.GameUtil;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameFactoryTest {

    private GameFactory gameFactory;

    @Before
    public void setUp() throws Exception {
        gameFactory = new GameFactory();
    }

    @Test
    public void shouldCreateGameObject() throws Exception {
        //given
        GameDto gameDto = GameUtil.mockGameDto();

        //when
        Game result = gameFactory.create(gameDto);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getPrice())
                .isNotNull()
                .isEqualTo(GameUtil.PRICE);
        assertThat(result.getTitle())
                .isNotBlank()
                .isEqualTo(GameUtil.TITLE);
        assertThat(result.getDescription())
                .isNotBlank()
                .isEqualTo(GameUtil.DESCRIPTION);
    }

    @Test
    public void shouldCreateTwoDifferentObject() throws Exception {
        //given
        GameDto gameDto = GameUtil.mockGameDto();

        Game game = gameFactory.create(gameDto);

        //when
        Game result = gameFactory.create(gameDto);

        //then
        assertThat(result)
                .isNotSameAs(game);
    }
}
