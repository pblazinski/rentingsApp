package pl.lodz.p.edu.grs.jobs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.factory.BorrowFactory;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.service.impl.BorrowServiceImpl;
import pl.lodz.p.edu.grs.util.BorrowUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PenaltySchedulerTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private BorrowFactory borrowFactory;

    private PenaltyScheduler penaltyScheduler;

    @Before
    public void setUp() throws Exception {
        borrowRepository = mock(BorrowRepository.class);
        penaltyScheduler = new PenaltyScheduler(borrowRepository, userService);
    }

    @Test
    public void shouldSetBorrowPenaltyForReturnedAfterTimeGame() {
        //given
        Borrow borrow = BorrowUtil.mockBorrow();
        borrow.setDeadline(LocalDateTime.now().minusDays(5));
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
}
