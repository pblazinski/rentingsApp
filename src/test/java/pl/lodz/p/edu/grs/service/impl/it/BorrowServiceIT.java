package pl.lodz.p.edu.grs.service.impl.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
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

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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

    private static final double DISCOUNT = 0.9;

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

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

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

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

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
    public void shouldAddBorrowAndSetGameNotAvailableAndDiscountPrice() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Game game1 = gameService.addGame(new GameDto("1", "1", true, 20.99, category.getId()));
        Game game2 = gameService.addGame(new GameDto("2", "1", true, 20.99, category.getId()));
        Game game3 = gameService.addGame(new GameDto("3", "1", true, 20.99, category.getId()));
        Game game4 = gameService.addGame(new GameDto("4", "1", true, 20.99, category.getId()));
        Game game5 = gameService.addGame(new GameDto("5", "1", true, 20.99, category.getId()));
        Game game6 = gameService.addGame(new GameDto("6", "1", true, 20.99, category.getId()));
        Game game7 = gameService.addGame(new GameDto("7", "1", true, 20.99, category.getId()));
        Game game8 = gameService.addGame(new GameDto("8", "1", true, 20.99, category.getId()));
        Game game9 = gameService.addGame(new GameDto("9", "1", true, 20.99, category.getId()));
        Game game10 = gameService.addGame(new GameDto("10", "1", true, 20.99, category.getId()));

        borrowService.addBorrow(new BorrowDto(Arrays.asList(game.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game1.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game2.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game3.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game5.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game6.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game7.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game8.getId())), registerUserDTO.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game9.getId())), registerUserDTO.getEmail());


        //when
        Borrow result = borrowService
                .addBorrow(new BorrowDto(Arrays.asList(game10.getId())), registerUserDTO.getEmail());

        //then
        assertThat(result.getBorrowedGames().get(0).getId())
                .isEqualTo(game10.getId());
        assertThat(result.getUser().getEmail())
                .isEqualTo(user.getEmail());
        assertThat(result.getBorrowedGames().get(0).isAvailable())
                .isEqualTo(false);
        assertThat(result.getTotalPrice())
                .isEqualTo(game10.getPrice() * DISCOUNT);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowContraintViolationExceptionWhenAddBorrowWithEmptyGamesList() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        BorrowDto borrowDto = new BorrowDto(Collections.<Long>emptyList());

        //when
        Borrow result = borrowService.addBorrow(borrowDto, registerUserDTO.getEmail());
        //then
    }

    @Test
    public void shouldGetOneBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //when
        Borrow result = borrowService.getBorrow(borrow.getId());

        //
        assertThat(borrow.getBorrowedGames().get(0).getId())
                .isSameAs(result.getBorrowedGames().get(0).getId());
        assertThat(borrow.getTimeBorrowed())
                .isEqualByComparingTo(result.getTimeBorrowed());
        assertThat(borrow.getDeadline())
                .isEqualByComparingTo(result.getDeadline());
        assertThat(borrow.getTotalPrice())
                .isEqualTo(result.getTotalPrice());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenGetNonExistingBorrow() throws Exception {
        //given

        //when
        borrowService.getBorrow(1L);

        //then
    }

    @Test
    public void shouldUpdatePenalty() throws Exception {
        //given
        double penalty = 10;
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //when
        Borrow result = borrowService.updatePenalties(penalty, borrow.getId());

        //then
        assertThat(result.getId())
                .isEqualTo(borrow.getId());
        assertThat(borrow.getPenalties())
                .isNotSameAs(result.getPenalties());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdatePenaltyForNotExistingBorrow() {
        //given
        //when
        borrowService.updatePenalties(10, 1L);
        //then
    }

    @Test
    public void shouldRemoveBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());

        //when
        borrowService.removeBorrow(borrow.getId());

        boolean exists = borrowRepository.exists(borrow.getId());
        assertThat(exists)
                .isEqualTo(false);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenRemoveNotExistingBorrow() throws Exception {
        //given
        //when
        borrowService.removeBorrow(1L);
        //then
    }

    @Test
    public void shouldUpdateReturnTimeAndSetGamesToAvaiable() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        RegisterUserDto registerUserDTO = UserUtil.mockRegisterUserDTO();

        User user = userService.registerUser(registerUserDTO);

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), registerUserDTO.getEmail());
        //when
        Borrow result = borrowService.updateReturnTime(borrow.getId());

        //then
        assertThat(borrow.getTimeBack())
                .isNull();
        assertThat(result.getTimeBack())
                .isNotSameAs(borrow.getTimeBack());
        assertThat(result.getBorrowedGames().get(0).isAvailable())
                .isEqualTo(true);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionUpdateBackTimeForNotExistingBorrow() throws Exception {
        //given
        //when
        borrowService.updateReturnTime(1L);
        //then
    }
}
