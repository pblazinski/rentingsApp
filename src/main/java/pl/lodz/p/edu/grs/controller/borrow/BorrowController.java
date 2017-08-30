package pl.lodz.p.edu.grs.controller.borrow;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.service.BorrowService;

import java.security.Principal;

@RestController
@RequestMapping(value = "api/borrow")
@Api(value = "api/borrow", description = "Endpoints for game borrow management")
public class BorrowController {

    private BorrowService borrowService;

    @Autowired
    BorrowController(final BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    @ApiOperation(value = "Find all borrows")
    public Page<Borrow> getAllBorrows(@PageableDefault final Pageable pageable) {
        return borrowService.findAll(pageable);
    }

    @GetMapping("/my")
    @ApiOperation(value = "Find user borrows")
    public Page<Borrow> getAllUserBorrows(@PageableDefault final Pageable pageable,
                                          final Principal principal) {
        return borrowService.findUserBorrows(pageable, principal.getName());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get borrow by id")
    public Borrow getBorrow(@PathVariable final Long id) {
        return borrowService.getBorrow(id);
    }

    @PostMapping
    @ApiOperation(value = "Create borrow")
    public HttpEntity addBorrow(@RequestBody final BorrowDto borrowDto,
                                final Principal principal) {
        Borrow borrow = borrowService.addBorrow(borrowDto, principal.getName());

        return ResponseEntity.ok(borrow);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove borrow")
    public HttpEntity removeBorrow(@PathVariable Long id) {
        borrowService.removeBorrow(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "Return game back")
    public HttpEntity returnGame(@PathVariable final Long id) {
       Borrow borrow = borrowService.updateReturnTime(id);

       return ResponseEntity.ok(borrow);
    }
}
