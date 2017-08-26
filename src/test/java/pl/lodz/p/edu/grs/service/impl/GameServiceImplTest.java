package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.factory.GameFactory;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameFactory gameFactory;

    @Mock
    private CategoryService categoryService;

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        this.gameService = new GameServiceImpl(gameRepository, categoryService, gameFactory);
    }

    @Test
    public void shouldReturnPageOfGames() throws Exception {
        //given
        Pageable pageable = new PageRequest(0, 10);
        Game game = new Game();

        List<Game> games = Collections.singletonList(game);
        PageImpl<Game> gamesPage = new PageImpl<>(games);

        when(gameRepository.findAll(pageable))
                .thenReturn(gamesPage);

        //when
        Page<Game> result = gameService.findAll(pageable);

        //then
        assertThat(result)
                .isEqualTo(gamesPage);
        assertThat(result.getContent())
                .containsExactly(game);
        assertThat(result.getTotalElements())
                .isEqualTo(games.size());
    }

    @Test
    public void shouldAddGame() throws Exception {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Game game = GameUtil.mockGame();

        Category category = CategoryUtil.mockCategory();

        when(categoryService.findOne(category.getId()))
                .thenReturn(category);
        when(gameFactory.create(gameDto))
                .thenReturn(game);
        when(gameRepository.save(game))
                .thenReturn(game);


        //when
        Game result = gameService.addGame(gameDto);


        //verify
        verify(gameFactory)
                .create(gameDto);
        verify(gameRepository)
                .save(game);

        assertThat(result)
                .isNotNull();
        assertThat(result.getCategory())
                .isEqualTo(category);
        assertThat(result.getDescription())
                .isEqualTo(GameUtil.DESCRIPTION);
        assertThat(result.isAvailable())
                .isEqualTo(GameUtil.AVAILABLE);
        assertThat(result.getPrice())
                .isEqualTo(GameUtil.PRICE);
        assertThat(result.getTitle())
                .isEqualTo(GameUtil.TITLE);
    }

    @Test
    public void shouldUpdateGameTitleAndDescription() throws Exception {
        //given
        Game game = GameUtil.mockGame();

        String title = "New title";
        String description = "New description";

        when(gameRepository.getOne(game.getId()))
                .thenReturn(game);
        when(gameRepository.save(game))
                .thenReturn(game);

        //when
        Game result = gameService.updateTitleAndDescription(game.getId(), title, description);

        //then
        verify(gameRepository)
                .save(game);

        assertThat(result.getTitle())
                .isNotBlank()
                .isEqualTo(title);
        assertThat(result.getDescription())
                .isNotBlank()
                .isEqualTo(description);
    }

    @Test
    public void shouldUpdateGamePrice() throws Exception {
        //given
        Game game = GameUtil.mockGame();

        double price = 0.99;

        when(gameRepository.getOne(game.getId()))
                .thenReturn(game);
        when(gameRepository.save(game))
                .thenReturn(game);

        //when
        Game result = gameService.updatePrice(game.getId(), price);

        //then
        verify(gameRepository)
                .save(game);

        assertThat(result.getPrice())
                .isEqualTo(price);
    }

    @Test
    public void shouldUpdateGameAvailability() throws Exception {
        //given
        Game game = GameUtil.mockGame();

        boolean availability = false;

        when(gameRepository.getOne(game.getId()))
                .thenReturn(game);
        when(gameRepository.save(game))
                .thenReturn(game);

        //when
        Game result = gameService.updateAvailability(game.getId(), availability);

        //then
        verify(gameRepository)
                .save(game);

        assertThat(result.isAvailable())
                .isEqualTo(availability);
    }

    @Test
    public void shouldUpdateGameCategory() throws Exception {
        //given
        Game game = GameUtil.mockGame();

        Category category = new Category(1L, "UPDATED");

        when(gameRepository.getOne(game.getId()))
                .thenReturn(game);
        when(gameRepository.save(game))
                .thenReturn(game);
        when(categoryService.findOne(1L))
                .thenReturn(category);

        //when
        Game result = gameService.updateCategory(game.getId(), 1L);

        //then
        verify(gameRepository)
                .save(game);

        assertThat(result.getCategory())
                .isEqualTo(category);
    }

    @Test
    public void shouldDeleteGame() throws Exception {
        //given
        long gameId = 1L;
        Game game = GameUtil.mockGame();

        when(gameRepository.getOne(gameId))
                .thenReturn(game);

        //when
        gameService.remove(gameId);

        //then
        verify(gameRepository)
                .getOne(gameId);
        verify(gameRepository)
                .delete(game);
    }
}
