package pl.lodz.p.edu.grs.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.service.BorrowService;

import java.util.List;

@RestController
@RequestMapping("api/borrow")
@Api(value = "api/borrow", description = "Endpoints for game borrow management")
public class BorrowController {

    private BorrowService borrowService;

    @Autowired
    BorrowController(final BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<Borrow> getAllBorrows() {
        return borrowService.findAll();
    }

    @PostMapping
    public Borrow addBorrow(Borrow borrow) {
        return borrowService.addBorrow(borrow);
    }
}
