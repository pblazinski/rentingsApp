package pl.lodz.p.edu.grs.model.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private Game game;

    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final double PRICE = 100.00;
    private static final boolean AVAILABLE = true;
    private static final Rate RATE = new Rate(10L, 10L);

    @Before
    public void setUp() throws Exception {
        game = new Game();
    }

    @Test
    public void shouldSetGameTitleAndDescription() throws Exception {
        //given

        //when
        game.updateTitleAndDescription(TITLE, DESCRIPTION);

        //then
        assertThat(game.getTitle())
                .isNotBlank()
                .isEqualTo(TITLE);
        assertThat(game.getDescription())
                .isNotBlank()
                .isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldReplaceGameTitleAndDescription() throws Exception {
        //given
        String title = "title";
        String description = "description";
        game.updateTitleAndDescription(title, description);

        //when
        game.updateTitleAndDescription(TITLE, DESCRIPTION);

        //then
        assertThat(game.getTitle())
                .isNotBlank()
                .isNotSameAs(title)
                .isEqualTo(TITLE);
        assertThat(game.getDescription())
                .isNotBlank()
                .isNotSameAs(description)
                .isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldSetGamePrice() throws Exception {
        //given

        //when
        game.updatePrice(PRICE);

        //then
        assertThat(game.getPrice())
                .isEqualTo(PRICE);
    }

    @Test
    public void shouldReplaceGamePrice() throws Exception {
        //given
        double price = 1210.00;
        game.updatePrice(price);

        //when
        game.updatePrice(PRICE);

        //then
        assertThat(game.getPrice())
                .isNotSameAs(price)
                .isEqualTo(PRICE);
    }

    @Test
    public void shouldSetAvailability() throws Exception {
        //given

        //when
        game.updateAvailability(AVAILABLE);

        //then
        assertThat(game.isAvailable())
                .isEqualTo(AVAILABLE);
    }

    @Test
    public void shouldReplaceAvailability() throws Exception {
        //given
        boolean availability = false;
        game.updateAvailability(availability);

        //when
        game.updateAvailability(AVAILABLE);

        //then
        assertThat(game.isAvailable())
                .isNotSameAs(availability)
                .isEqualTo(AVAILABLE);
    }

    @Test
    public void shouldCreateGameWithRatingSummary() {
        //given

        //when

        //then
        RatingSummary ratingSummary = game.getRatingSummary();
        assertThat(ratingSummary)
                .isNotNull();
        assertThat(ratingSummary.getRates())
                .isEmpty();
    }

    @Test
    public void shouldAddRate() {
        //given

        //when
        game.addRate(RATE);

        //then
        RatingSummary ratingSummary = game.getRatingSummary();
        assertThat(ratingSummary.getRates())
                .hasSize(1)
                .containsOnly(RATE);

        assertThat(ratingSummary.getAverage())
                .isEqualTo(10.0);
    }

    @Test
    public void shouldNotAddRateWhenOneExist() {
        //given
        game.addRate(new Rate(10L, 10L));

        //when
        game.addRate(RATE);

        //then
        RatingSummary ratingSummary = game.getRatingSummary();
        assertThat(ratingSummary.getRates())
                .hasSize(1);
    }

    @Test
    public void shouldAddRateWhenSameRateButDifferentUserId() {
        //given
        game.addRate(new Rate(8L, 10L));

        //when
        game.addRate(RATE);

        //then
        RatingSummary ratingSummary = game.getRatingSummary();
        assertThat(ratingSummary.getRates())
                .hasSize(2);
        assertThat(ratingSummary.getRates())
                .contains(RATE);
    }

}