package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    private CategoryService categoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(gameRepository, categoryService);
    }

    @Test

    public void shouldAddGame() {
        //given
        Game game = new Game("title","desc", true,80);
        when(gameRepository.saveAndFlush(game))
                .thenReturn(game);

        //when
        Game game1 = gameService.addGame(game);

        //then
        verify(gameRepository)
                .saveAndFlush(game);
        assertThat(game.getTitle())
                .isEqualTo(game1.getTitle());
    }

    @Test
    public void shouldReturnListOfGame() {
        //given
        List<Game> list = Collections.singletonList(new Game());
        when(gameRepository.findAll())
                .thenReturn(list);

        //when
        List<Game> all = gameService.findAll();

        //then
        verify(gameRepository)
                .findAll();
        assertThat(list.size())
                .isEqualTo(all.size());
    }
}
