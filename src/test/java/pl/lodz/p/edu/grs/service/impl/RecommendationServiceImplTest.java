package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.RecommendationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lodz.p.edu.grs.util.GameUtil.GAME_ID;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationServiceImplTest {

    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private GameRepository gameRepository;
    private RecommendationService recommendationService;

    private static final long FIRST_GAME_ID = 3L;
    private static final long SECOND_GAME_ID = 4L;

    @Before
    public void setUp() throws Exception {
        recommendationService = new RecommendationServiceImpl(gameRepository, borrowRepository);
    }

    @Test
    public void shouldReturnGamesListBasedOnCategory() {
        //given
        Game firstGame = mock(Game.class);
        Game secondGame = mock(Game.class);

        List<Game> games = Arrays.asList(firstGame, secondGame);

        Borrow borrow = mock(Borrow.class);

        when(gameRepository.findAllWithoutSelectWithCategorySameAsSelectedGame(GAME_ID))
                .thenReturn(games);
        when(borrowRepository.findAllByBorrowedGamesIn(games))
                .thenReturn(Collections.singletonList(borrow));
        when(borrow.getBorrowedGames())
                .thenReturn(Collections.singletonList(secondGame));
        when(firstGame.getId())
                .thenReturn(FIRST_GAME_ID);
        when(secondGame.getId())
                .thenReturn(SECOND_GAME_ID);

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCategory(GAME_ID, 100);

        //then
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .containsExactly(secondGame);
    }

    @Test
    public void shouldReturnEmptyGamesListBasedOnCategoryWhenNotFoundAny() {
        //given
        Game firstGame = mock(Game.class);
        Game secondGame = mock(Game.class);

        List<Game> games = Arrays.asList(firstGame, secondGame);

        Borrow borrow = mock(Borrow.class);

        when(gameRepository.findAllWithoutSelectWithCategorySameAsSelectedGame(GAME_ID))
                .thenReturn(games);
        when(borrowRepository.findAllByBorrowedGamesIn(games))
                .thenReturn(Collections.singletonList(borrow));
        when(borrow.getBorrowedGames())
                .thenReturn(Collections.emptyList());

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCategory(GAME_ID, 100);

        //then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void shouldReturnGamesListBasedOnCollaborationRecommendation() {
        //given
        Game firstGame = mock(Game.class);
        Game secondGame = mock(Game.class);

        List<Long> games = Arrays.asList(FIRST_GAME_ID, SECOND_GAME_ID);

        Borrow borrow = mock(Borrow.class);


        when(borrowRepository.findAllByBorrowedGamesId(GAME_ID))
                .thenReturn(Collections.singletonList(borrow));
        when(borrow.getBorrowedGames())
                .thenReturn(Arrays.asList(firstGame, secondGame));

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCollaboration(GAME_ID, 100);

        //then
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .contains(firstGame, secondGame);
    }
}