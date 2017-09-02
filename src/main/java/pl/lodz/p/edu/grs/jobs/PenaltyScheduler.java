package pl.lodz.p.edu.grs.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class PenaltyScheduler {

    private BorrowRepository borrowRepository;

    private UserService userService;

    private static double PENALTY = 0.25;

    public PenaltyScheduler(final BorrowRepository borrowRepository,
                            final UserService userService) {
        this.borrowRepository = borrowRepository;
        this.userService = userService;
    }


    @Scheduled(cron = "0 0 6 * * *")
    public void countPenaltiesScheduler() {
        LocalDateTime now = LocalDateTime.now();
        List<Borrow> borrowsUnReturned = borrowRepository.findBorrowsByTimeBack(null);

        for (Borrow borrow : borrowsUnReturned) {
            if (borrow.getDeadline().isBefore(now)) {
                borrow.updatePenalties(borrow.getPenalties() + borrow.getTotalPrice() * PENALTY);

                if (borrow.getPenalties() == borrow.getTotalPrice() * 0.75) {
                    userService.blockUser(borrow.getId());
                }

                log.info("Set borrow <{}> penalty to <{}>", borrow.getId(), borrow.getPenalties());
                borrowRepository.save(borrow);
            }
        }
    }
}
