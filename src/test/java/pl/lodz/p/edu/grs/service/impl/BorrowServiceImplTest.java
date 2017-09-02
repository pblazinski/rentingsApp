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
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.factory.BorrowFactory;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.BorrowUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.UserUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BorrowServiceImplTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private BorrowFactory borrowFactory;

    private BorrowService borrowService;

    private static final double DISCOUNT = 0.9;

    @Before
    public void setUp() throws Exception {
        borrowRepository = mock(BorrowRepository.class);
        this.borrowService = new BorrowServiceImpl(borrowRepository, gameService, userService, borrowFactory);
    }


    @Test
    public void shouldReturnPageOfBorrows() throws Exception {
        //given
        Pageable pageable = new PageRequest(0, 10);
        Borrow borrow = new Borrow();

        List<Borrow> borrows = Collections.singletonList(borrow);
        PageImpl<Borrow> borrowsPage = new PageImpl<>(borrows);

        when(borrowRepository.findAll(pageable))
                .thenReturn(borrowsPage);

        //when
        Page<Borrow> result = borrowService.findAll(pageable);

        //then
        assertThat(result)
                .isEqualTo(borrowsPage);
        assertThat(result.getContent())
                .containsExactly(borrow);
        assertThat(result.getTotalElements())
                .isEqualTo(borrows.size());
    }

    @Test
    public void shouldReturnUserPageOfBorrows() throws Exception {
        //given
        Pageable pageable = new PageRequest(0, 10);
        Borrow borrow = new Borrow();
        User user = UserUtil.mockUser();
        borrow.setUser(user);
        List<Borrow> borrows = Collections.singletonList(borrow);
        PageImpl<Borrow> borrowsPage = new PageImpl<>(borrows);

        when(borrowRepository.findBorrowsByUserEmail(pageable, user.getEmail()))
                .thenReturn(borrowsPage);


        //when
        Page<Borrow> result = borrowService.findUserBorrows(pageable, user.getEmail());

        //then

        assertThat(result)
                .isEqualTo(borrowsPage);
        assertThat(result.getContent())
                .containsExactly(borrow);
        assertThat(result.getTotalElements())
                .isEqualTo(borrows.size());
    }

    @Test
    public void shouldAddBorrowAndSetGamesNotAvailableAndDiscoutGames() throws Exception {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();
        User user = UserUtil.mockUser();
        Game game = GameUtil.mockGame();
        borrow.setBorrowedGames(Collections.singletonList(game));
        borrow.setUser(user);

        List<Borrow> borrows = Arrays.asList(borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow, borrow);
        PageImpl<Borrow> borrowsPage = new PageImpl<>(borrows);

        when(borrowRepository.findBorrowsByUserEmail(new PageRequest(0, 20), user.getEmail()))
                .thenReturn(borrowsPage);
        when(borrowFactory.create(Collections.singletonList(game), user))
                .thenReturn(borrow);
        when(userService.findByEmail(UserUtil.EMAIL))
                .thenReturn(user);
        when(gameService.getGame(1L))
                .thenReturn(game);
        when(borrowRepository.save(borrow))
                .thenReturn(borrow);

        //when
        Borrow result = borrowService.addBorrow(new BorrowDto(Collections.singletonList(1L)), UserUtil.EMAIL);

        //then
        verify(borrowRepository)
                .save(borrow);

        assertThat(result.getBorrowedGames().get(0).isAvailable())
                .isEqualTo(false);
        assertThat(result.getTotalPrice())
                .isEqualTo(BorrowUtil.mockBorrow().getTotalPrice() * DISCOUNT);

    }

    @Test
    public void shouldGetBorrowWithId() throws Exception {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();

        when(borrowRepository.findOne(borrow.getId()))
                .thenReturn(borrow);
        when(borrowRepository.exists(borrow.getId()))
                .thenReturn(true);


        //when
        Borrow result = borrowService.getBorrow(borrow.getId());


        //then
        verify(borrowRepository)
                .findOne(borrow.getId());
        verify(borrowRepository)
                .exists(borrow.getId());

        assertThat(result.getBorrowedGames())
                .isEqualTo(borrow.getBorrowedGames());
        assertThat(result.getUser())
                .isEqualTo(result.getUser());
    }

    @Test
    public void shouldUpdatePenalty() throws Exception {
        //given
        double price = 99;
        Borrow borrow = BorrowUtil.mockBorrow();

        when(borrowRepository.findOne(borrow.getId()))
                .thenReturn(borrow);
        when(borrowRepository.exists(borrow.getId()))
                .thenReturn(true);

        //when
        Borrow result = borrowService.updatePenalties(price, borrow.getId());


        //then
        verify(borrowRepository)
                .findOne(borrow.getId());

        assertThat(result.getPenalties())
                .isNotNull()
                .isEqualTo(price)
                .isNotSameAs(borrow.getTotalPrice());
        assertThat(result.getBorrowedGames())
                .isEqualTo(borrow.getBorrowedGames());
        assertThat(result.getUser())
                .isEqualTo(borrow.getUser());
    }


    @Test
    public void shouldRemoveBorrow() throws Exception {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();

        when(borrowRepository.exists(borrow.getId()))
                .thenReturn(true);
        //when
        borrowService.removeBorrow(borrow.getId());

        //then
        verify(borrowRepository)
                .delete(borrow.getId());
    }


    @Test
    public void shouldGiveBackGamesAndSetThemToAvailable() throws Exception {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();

        when(borrowRepository.exists(borrow.getId()))
                .thenReturn(true);
        when(borrowRepository.findOne(borrow.getId()))
                .thenReturn(borrow);
        when(borrowRepository.save(borrow))
                .thenReturn(borrow);

        //when
        Borrow result = borrowService.updateReturnTime(borrow.getId());

        //then
        assertThat(result.getBorrowedGames().get(0).isAvailable())
                .isEqualTo(true);
        assertThat(result.getTimeBack())
                .isNotNull();
    }
}
