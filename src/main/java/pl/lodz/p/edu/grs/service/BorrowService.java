package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.model.Borrow;


public interface BorrowService {

    Page<Borrow> findAll(Pageable pageable);

    Page<Borrow> findUserBorrows(Pageable pageable, String principal);

    Borrow getBorrow(Long id);

    Borrow updatePenalties(double value, Long id);

    Borrow addBorrow(BorrowDto borrowDto, String principal);

    void removeBorrow(Long id);

    Borrow updateReturnTime(Long id);
}
