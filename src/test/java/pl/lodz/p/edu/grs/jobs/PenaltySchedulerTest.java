package pl.lodz.p.edu.grs.jobs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.BorrowUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PenaltySchedulerTest {

    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private UserService userService;
    private PenaltyScheduler penaltyScheduler;

    private static final double PENALTY = 0.75;
    private static final double TOTAL_PRICE = 1.00;

    @Before
    public void setUp() throws Exception {
        borrowRepository = mock(BorrowRepository.class);
        penaltyScheduler = new PenaltyScheduler(borrowRepository, userService);
    }

    @Test
    public void shouldSetBorrowPenaltyForReturnedAfterTimeGame() {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();
        borrow.updateDeadline(LocalDateTime.now().minusDays(5));
        List<Borrow> borrows = Collections.singletonList(borrow);

        when(borrowRepository.findBorrowsByTimeBack(null))
                .thenReturn(borrows);
        when(borrowRepository.save(borrow))
                .thenReturn(borrow);

        //when
        penaltyScheduler.countPenaltiesScheduler();

        //then
        verify(borrowRepository)
                .findBorrowsByTimeBack(null);
        verify(borrowRepository)
                .save(borrow);
    }

    @Test
    public void shouldBlockUserWhenPenaltiesIsEqualToTotal() {
        //given
        Borrow borrow = mock(Borrow.class);
        List<Borrow> borrows = Collections.singletonList(borrow);

        when(borrowRepository.findBorrowsByTimeBack(null))
                .thenReturn(borrows);
        when(borrow.getDeadline())
                .thenReturn(LocalDateTime.now().minusHours(5));
        when(borrow.getPenalties())
                .thenReturn(PENALTY);
        when(borrow.getTotalPrice())
                .thenReturn(TOTAL_PRICE);
        when(borrow.getId())
                .thenReturn(BorrowUtil.BORROW_ID);
        when(borrowRepository.save(borrow))
                .thenReturn(borrow);

        //when
        penaltyScheduler.countPenaltiesScheduler();

        //then
        verify(borrowRepository)
                .findBorrowsByTimeBack(null);
        verify(userService)
                .blockUser(BorrowUtil.BORROW_ID);
        verify(borrowRepository)
                .save(borrow);
    }

    @Test
    public void shouldNotChangeAnythingWhenGetDeadlineIsAfterNow() {
        //given
        Borrow borrow = mock(Borrow.class);
        List<Borrow> borrows = Collections.singletonList(borrow);

        when(borrowRepository.findBorrowsByTimeBack(null))
                .thenReturn(borrows);
        when(borrow.getDeadline())
                .thenReturn(LocalDateTime.now().plusDays(5));

        //when
        penaltyScheduler.countPenaltiesScheduler();

        //then
        verify(borrowRepository)
                .findBorrowsByTimeBack(null);

        verifyZeroInteractions(userService);
        verifyZeroInteractions(borrowRepository);
    }
}
