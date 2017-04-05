package pl.lodz.p.edu.grs.controller;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.impl.GameServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(gameRepository);
    }

    @Test
    public void gameAdd_shouldAdd() {
        Game game = new Game("title","desc", true);

        when(gameRepository.saveAndFlush(game)).thenReturn(game);

        Game game1 = gameService.addGame(game);

        verify(gameRepository,times(1)).saveAndFlush(game);

        Assertions.assertThat(game.getTitle())
                .isEqualTo(game1.getTitle());

    }

    @Test
    public void findAll_ShouldReturn1() {
        List<Game> list = Collections.singletonList(new Game());

        when(gameRepository.findAll()).thenReturn(list);

        List<Game> all = gameService.findAll();

        verify(gameRepository, times(1)).findAll();

        Assertions.assertThat(list.size())
                .isEqualTo(all.size());
    }
}
