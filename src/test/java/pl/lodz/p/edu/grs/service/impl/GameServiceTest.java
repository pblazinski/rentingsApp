package pl.lodz.p.edu.grs.service.impl;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.impl.CategoryServiceImpl;
import pl.lodz.p.edu.grs.service.impl.GameServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private GameService gameService;

    private CategoryService categoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(gameRepository, categoryService);
        categoryService = new CategoryServiceImpl(gameRepository, categoryRepository);
    }

    @Test
    @Ignore("Is modified in other branch")
    public void gameAdd_shouldAdd() {
        Game game = new Game("title","desc", true,80);

        Category category = new Category("FPS");

        when(categoryRepository.findOne(1L)).thenReturn(category);
        when(gameRepository.saveAndFlush(game)).thenReturn(game);


        Game game1 = gameService.addGame(game, 1L);

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
