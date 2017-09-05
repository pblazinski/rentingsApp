package pl.lodz.p.edu.grs.service.impl.it;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.game.RateDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.exceptions.GameAddRateException;
import pl.lodz.p.edu.grs.exceptions.NotFoundException;
import pl.lodz.p.edu.grs.exceptions.RateAddException;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.game.Rate;
import pl.lodz.p.edu.grs.model.game.RatingSummary;
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
import pl.lodz.p.edu.grs.util.StubHelper;
import pl.lodz.p.edu.grs.util.UserUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameServiceIT {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BorrowService borrowService;

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

    private static final String BLANK_VALUE = "     ";

    private static final double BELOW_ZERO = -1;

    @After
    public void tearDown() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void shouldReturnMostPopularGame() {
        //
        RegisterUserDto userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);

        Category category = categoryService.addCategory(new CategoryDto("1"));

        Game game = gameService.addGame(new GameDto("1", "1", true, 10, category.getId()));
        Game game1 = gameService.addGame(new GameDto("2", "1", true, 10, category.getId()));
        Game game2 = gameService.addGame(new GameDto("3", "1", true, 10, category.getId()));
        Game game3 = gameService.addGame(new GameDto("4", "1", true, 10, category.getId()));
        Game game4 = gameService.addGame(new GameDto("5", "1", true, 10, category.getId()));

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId(), game1.getId(), game2.getId())), user.getEmail());
        Borrow borrow2 = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId(), game1.getId(), game3.getId(), game.getId())), user.getEmail());
        Borrow borrow3 = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId())), user.getEmail());


        //when
        List<Game> games = gameService.getMostPopular(2L);

        //
        assertThat(games.size())
                .isEqualTo(2);
        assertThat(games.get(0).getId())
                .isEqualTo(game4.getId());
        assertThat(games.get(1).getId())
                .isEqualTo(game1.getId());
    }


    @Test
    public void shouldReturnPageWithOneGame() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        //when
        Page<Game> games = gameService.findAll(new PageRequest(0, 10));

        assertThat(games.getTotalElements())
                .isEqualTo(1);

        Game result = games.getContent().get(0);

        assertThat(result.getTitle())
                .isEqualTo(game.getTitle());
        assertThat(result.getDescription())
                .isEqualTo(game.getDescription());
        assertThat(result.getPrice())
                .isEqualTo(game.getPrice());
        assertThat(result.getCategory().getId())
                .isEqualTo(category.getId());
        assertThat(result.isAvailable())
                .isEqualTo(game.isAvailable());
    }

    @Test
    public void shouldAddGame() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        //when
        Game game = gameService.addGame(gameDto);

        //then
        assertThat(game)
                .isNotNull();
        assertThat(game.getTitle())
                .isNotEmpty()
                .isEqualTo(GameUtil.TITLE);
        assertThat(game.getDescription())
                .isNotEmpty()
                .isEqualTo(GameUtil.DESCRIPTION);
        assertThat(game.getPrice())
                .isNotNull()
                .isEqualTo(GameUtil.PRICE);
        assertThat(game.isAvailable())
                .isNotNull()
                .isEqualTo(GameUtil.AVAILABLE);
        assertThat(game.getCategory().getId())
                .isNotNull()
                .isEqualTo(category.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenAddGameWithBlankTitle() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setTitle(BLANK_VALUE);
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        //when
        gameService.addGame(gameDto);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenAddGameWithBlankDescription() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setDescription(BLANK_VALUE);
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        //when
        gameService.addGame(gameDto);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenAddGameWithBelowZeroPrice() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setPrice(BELOW_ZERO);
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        //when
        gameService.addGame(gameDto);
        //then
    }

    @Test
    public void shouldRemoveGame() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();
        //when
        gameService.remove(id);

        //then
        Boolean exists = gameRepository.exists(id);
        assertThat(exists)
                .isFalse();
    }

    @Test
    public void shouldUpdateTitleAndDescription() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        String title = "Updated Title";
        String description = " Updated Description";

        //when
        Game result = gameService.updateTitleAndDescription(id, title, description);

        //then
        assertThat(result.getTitle())
                .isNotBlank()
                .isEqualTo(title);
        assertThat(result.getDescription())
                .isNotBlank()
                .isEqualTo(description);
    }

    @Test
    public void shouldUpdatePrice() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        double price = 9999;

        //when
        Game result = gameService.updatePrice(id, price);

        //then
        assertThat(result.getPrice())
                .isEqualTo(price);
    }

    @Test
    public void shouldUpdateAvailability() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        Boolean available = false;

        //when
        Game result = gameService.updateAvailability(id, available);

        //then
        assertThat(result.isAvailable())
                .isEqualTo(available);
    }

    @Test
    public void shouldUpdateCategory() {
        //given
        GameDto gameDto = GameUtil.mockGameDto();
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        Category category1 = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        //when
        Game result = gameService.updateCategory(id, category1.getId());

        //then
        assertThat(result.getCategory().getId())
                .isEqualTo(category1.getId());
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdateTitleAndDescriptionWithBlankValues() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        //when
        Throwable throwable = catchThrowable(() -> gameService.updateTitleAndDescription(id, BLANK_VALUE, BLANK_VALUE));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdatePriceBelowZero() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        //when
        Throwable throwable = catchThrowable(() -> gameService.updatePrice(id, BELOW_ZERO));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateTitleAndDescriptionForNotExistedGame() {
        //given

        //when
        gameService.updateTitleAndDescription(1L, BLANK_VALUE, BLANK_VALUE);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdatePriceForNotExistedGame() {
        //given

        //when
        gameService.updatePrice(1L, BELOW_ZERO);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateAvailabilityForNotExistedGame() {
        //given

        //when
        gameService.updateAvailability(1L, false);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateCategoryForNotExistedGame() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        //when
        gameService.updateCategory(100L, category.getId());

        //then
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenUpdateCategoryForNotExistedCategory() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        //when
        gameService.updateCategory(game.getId(), 100L);

        //then
    }

    @Test
    public void shouldAddRate() {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        //when
        Game result = gameService.addRate(game.getId(), rateDto, user.getEmail());

        //then
        RatingSummary ratingSummary = result.getRatingSummary();

        assertThat(ratingSummary)
                .isNotNull();
        assertThat(ratingSummary.getAverage())
                .isEqualTo(rateDto.getRate());
        Set<Rate> rates = ratingSummary.getRates();
        Rate rate = rates.iterator().next();
        assertThat(rates)
                .isNotEmpty()
                .hasSize(1);
        assertThat(rate.getRate())
                .isNotNull()
                .isEqualTo(rateDto.getRate());
        assertThat(rate.getComment())
                .isNotBlank()
                .isEqualTo(rateDto.getComment());
        assertThat(rate.getUserId())
                .isNotNull()
                .isEqualTo(user.getId());
    }

    @Test
    public void shouldThrowRateAddExceptionWhenUserTryToAddSecondRate() {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        gameService.addRate(game.getId(), rateDto, user.getEmail());
        //when
        Throwable throwable = catchThrowable(() -> gameService.addRate(game.getId(), rateDto, user.getEmail()));

        //then
        assertThat(throwable)
                .isInstanceOf(RateAddException.class)
                .hasMessage("User can add only one rate for one game");
    }

    @Test
    public void shouldThrowGameAddRateExceptionWhenUserTryAddRateForGameWhichNotBorrow() {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        gameService.addRate(game.getId(), rateDto, user.getEmail());
        //when
        Throwable throwable = catchThrowable(() -> gameService.addRate(game.getId() + 100L, rateDto, user.getEmail()));

        //then
        assertThat(throwable)
                .isInstanceOf(GameAddRateException.class)
                .hasMessage("User cannot add rate for game which not borrow.");
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenAddRateWithTooBigRate() {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(11)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        //when
        Throwable throwable = catchThrowable(() -> gameService.addRate(game.getId(), rateDto, user.getEmail()));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);

        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenAddRateWithTooLongComment() {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(1)
                .comment(StringUtils.repeat("a", 256))
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        //when
        Throwable throwable = catchThrowable(() -> gameService.addRate(game.getId(), rateDto, user.getEmail()));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);

        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }
}
