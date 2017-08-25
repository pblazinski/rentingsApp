package pl.lodz.p.edu.grs.factory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class GameFactoryTest {

    private GameFactory gameFactory;

    public static final String TITLE = "Title";

    public static final String DESCRIPTION = "Description";

    public static final boolean AVAILABLE = true;

    public static final LocalDateTime TIME = LocalDateTime.of(1, 1, 1, 1, 1);

    public static final double PRICE = 20.99;

    @Before
    public void setUp() throws Exception {
        gameFactory = new GameFactory();
    }


}
