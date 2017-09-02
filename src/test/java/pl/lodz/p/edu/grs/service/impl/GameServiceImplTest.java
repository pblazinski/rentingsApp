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
import pl.lodz.p.edu.grs.controller.game.RateDto;
import pl.lodz.p.edu.grs.factory.GameFactory;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.game.Rate;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static pl.lodz.p.edu.grs.util.BorrowUtil.BORROW_ID;
import static pl.lodz.p.edu.grs.util.BorrowUtil.mockBorrow;
import static pl.lodz.p.edu.grs.util.GameUtil.AVAILABLE;
import static pl.lodz.p.edu.grs.util.GameUtil.DESCRIPTION;
import static pl.lodz.p.edu.grs.util.GameUtil.GAME_ID;
import static pl.lodz.p.edu.grs.util.GameUtil.PRICE;
import static pl.lodz.p.edu.grs.util.GameUtil.TITLE;
import static pl.lodz.p.edu.grs.util.GameUtil.mockGame;
import static pl.lodz.p.edu.grs.util.GameUtil.mockGameDto;
import static pl.lodz.p.edu.grs.util.UserUtil.EMAIL;
import static pl.lodz.p.edu.grs.util.UserUtil.USER_ID;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameFactory gameFactory;
    @Mock
    private CategoryService categoryService;

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        this.gameService = new GameServiceImpl(gameRepository, categoryService, gameFactory, borrowRepository);
    }


    @Test
    public void shouldReturnMostPopularGame() throws Exception {
        //given
        List<Borrow> borrows = Collections.singletonList(mockBorrow());

        when(borrowRepository.findAll())
                .thenReturn(borrows);
        when(gameRepository.getOne(borrows.get(0).getBorrowedGames().get(0).getId()))
                .thenReturn(borrows.get(0).getBorrowedGames().get(0));

        //when
        List<Game> result = gameService.getMostPopular(1L);


        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.size())
                .isEqualTo(1);
        assertThat(result.get(0))
                .isEqualTo(borrows.get(0).getBorrowedGames().get(0));
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
        GameDto gameDto = mockGameDto();
        Game game = mockGame();

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
                .isEqualTo(DESCRIPTION);
        assertThat(result.isAvailable())
                .isEqualTo(AVAILABLE);
        assertThat(result.getPrice())
                .isEqualTo(PRICE);
        assertThat(result.getTitle())
                .isEqualTo(TITLE);
    }

    @Test
    public void shouldUpdateGameTitleAndDescription() throws Exception {
        //given
        Game game = mockGame();

        String title = "New title";
        String description = "New description";

        when(gameRepository.exists(game.getId()))
                .thenReturn(true);
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
        Game game = mockGame();

        double price = 0.99;
        when(gameRepository.exists(game.getId()))
                .thenReturn(true);
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
        Game game = mockGame();

        boolean availability = false;
        when(gameRepository.exists(game.getId()))
                .thenReturn(true);
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
        Game game = mockGame();

        Category category = new Category(1L, "UPDATED");
        when(gameRepository.exists(game.getId()))
                .thenReturn(true);
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
        Game game = mockGame();
        when(gameRepository.exists(game.getId()))
                .thenReturn(true);
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

    @Test
    public void shouldAddRate() throws Exception {
        //given
        Game game = mock(Game.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        RateDto rateDto = RateDto.builder()
                .borrowId(BORROW_ID)
                .comment("comment")
                .rate(10)
                .build();

        when(borrowRepository.findByIdAndUserEmailAndBorrowedGamesIdIn(BORROW_ID, EMAIL, GAME_ID))
                .thenReturn(Optional.of(borrow));
        when(borrow.getUser())
                .thenReturn(user);
        when(borrow.getBorrowedGames())
                .thenReturn(Collections.singletonList(game));
        when(game.getId())
                .thenReturn(GAME_ID);
        //when
        gameService.addRate(USER_ID, rateDto, EMAIL);

        //then
        verify(borrowRepository)
                .findByIdAndUserEmailAndBorrowedGamesIdIn(BORROW_ID, EMAIL, GAME_ID);
        verify(borrow)
                .getUser();
        verify(borrow)
                .getBorrowedGames();
        verify(game)
                .addRate(any(Rate.class));
        verify(gameRepository)
                .save(game);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenUserAddRateForGameWhichNotBorrow() throws Exception {
        //given
        Game game = mock(Game.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        RateDto rateDto = RateDto.builder()
                .borrowId(BORROW_ID)
                .comment("comment")
                .rate(10)
                .build();

        when(borrowRepository.findByIdAndUserEmailAndBorrowedGamesIdIn(BORROW_ID, EMAIL, GAME_ID))
                .thenReturn(Optional.empty());
        //when
        gameService.addRate(USER_ID, rateDto, EMAIL);

        //then
        verify(borrowRepository)
                .findByIdAndUserEmailAndBorrowedGamesIdIn(BORROW_ID, EMAIL, GAME_ID);
        verifyZeroInteractions(borrow);
        verifyZeroInteractions(game);
        verifyZeroInteractions(user);
        verifyZeroInteractions(gameRepository);
    }

}
