package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;

    private final GameService gameService;

    private final UserService userService;

    @Autowired
    public BorrowServiceImpl(final BorrowRepository borrowRepository,
                             final GameService gameService,
                             final UserService userService) {
        this.borrowRepository = borrowRepository;
        this.gameService = gameService;
        this.userService = userService;
    }


    @Override
    public Page<Borrow> findAll(final Pageable pageable) {
        Page<Borrow> borrows = borrowRepository.findAll(pageable);

        log.info("Found <{}> borrows page", borrows.getTotalElements());

        return borrows;
    }

    @Override
    public Page<Borrow> findUserBorrows(final Pageable pageable, final String principal) {
        Page<Borrow> userBorrows = borrowRepository.findBorrowsByUser_Email(pageable, principal);

        log.info("Found <{}> borrows page borrowed by <{}>", userBorrows.getTotalElements(), principal);

        return userBorrows;
    }

    @Override
    public Borrow getBorrow(final Long id) {
        if(!borrowRepository.exists(id)) {
            throw new EntityNotFoundException("Borrow entity not found");
        }

        Borrow borrow = borrowRepository.findOne(id);

        log.info("Found borrow with id", borrow.getId());

        return borrow;
    }

    @Override
    public Borrow updatePenalties(final double value, final Long id) {
        if(!borrowRepository.exists(id)) {
            throw new EntityNotFoundException("Borrow entity not found");
        }
        Borrow borrow = borrowRepository.findOne(id);

        borrow.setPenalties(value);

        log.info("Updated borrow <{}> with value <{}>", borrow.getId(), value);

        borrowRepository.save(borrow);

        return borrow;
    }

    @Override
    public Borrow addBorrow(final List<Long> borrows, final String principal) {
        List<Game> borrowedGames = new ArrayList<>();
        borrows.forEach(id -> borrowedGames.add(gameService.getGame(id)));

        User user = userService.findByEmail(principal);

        Borrow borrow = new Borrow(borrowedGames, user);

        borrow.setTotalPrice(borrowedGames
                .stream()
                .mapToDouble(Game::getPrice)
                .sum());

        borrow = borrowRepository.save(borrow);

        //log.info("Add borrow <{}> with <{}> games", borrow.getId(), borrow.getBorrowedGames().size());

        return borrow;
    }

    @Override
    public void removeBorrow(final Long id) {
        if(!borrowRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
        borrowRepository.delete(id);

        log.info("Removed borrow <{}> ", id);
    }
}
