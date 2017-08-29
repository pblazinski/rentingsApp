package pl.lodz.p.edu.grs.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.UserUtil;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BorrowServiceIT {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @After
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void tearDown() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnPageWithOneBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDTO registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //when
        Page<Borrow> borrows = borrowService.findAll(new PageRequest(0, 10));

        //then
        assertThat(borrows.getTotalElements())
                .isEqualTo(1);

        Borrow result = borrows.getContent().get(0);

        assertThat(result.getUser().getId())
                .isEqualTo(user.getId());
        assertThat(result.getBorrowedGames().get(0).getId())
                .isEqualTo(game.getId());
        assertThat(result.getTotalPrice())
                .isEqualTo(result.getBorrowedGames().get(0).getPrice());
    }

    @Test
    public void shouldReturnPageWithOneUserBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDTO registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //when
        Page<Borrow> borrows = borrowService.findUserBorrows(new PageRequest(0, 10), user.getEmail());

        //then
        assertThat(borrows.getTotalElements())
                .isEqualTo(1);

        Borrow result = borrows.getContent().get(0);

        assertThat(result.getUser().getId())
                .isEqualTo(user.getId());
        assertThat(result.getBorrowedGames().get(0).getId())
                .isEqualTo(game.getId());
        assertThat(result.getTotalPrice())
                .isEqualTo(result.getBorrowedGames().get(0).getPrice());
    }

    @Test
    public void shouldAddBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDTO registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        //when
        Borrow result = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //then
        assertThat(result.getBorrowedGames().get(0).getId())
                .isEqualTo(game.getId());
        assertThat(result.getUser().getEmail())
                .isEqualTo(user.getEmail());
        assertThat(result.getTotalPrice())
                .isEqualTo(game.getPrice());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowContraintViolationExceptionWhenAddBorrowWithEmptyGamesList() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDTO registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        BorrowDto borrowDto = new BorrowDto(Collections.<Long>emptyList());

        //when
        Borrow result = borrowService.addBorrow(borrowDto, registerUserDTO.getEmail());
        //then
    }




}
