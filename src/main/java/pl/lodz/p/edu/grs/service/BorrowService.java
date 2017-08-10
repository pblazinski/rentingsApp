package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.model.Borrow;

import java.util.List;


public interface BorrowService {

    Page<Borrow> findAll(Pageable pageable);

    List<Borrow> findAll();

    Borrow addBorrow(Borrow borrow);
}
