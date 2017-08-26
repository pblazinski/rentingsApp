package pl.lodz.p.edu.grs.service.impl;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GameServiceIT {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameService gameService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String BLANK_VALUE = "     ";

    private static final double BELOW_ZERO = -1;

    @After
    public void tearDown() throws Exception {
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
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

    @Test(expected = TransactionSystemException.class)
    public void shouldThrowTransactionSystemExceptionWhenUpdateTitleAndDescriptionWithBlankValues() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        //when
        gameService.updateTitleAndDescription(id, BLANK_VALUE, BLANK_VALUE);
        //then
    }

    @Test(expected = TransactionSystemException.class)
    public void shouldThrowTransactionSystemExceptionWhenUpdatePriceBelowZero() {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        Game game = gameService.addGame(gameDto);
        Long id = game.getId();

        //when
        gameService.updatePrice(id, BELOW_ZERO);
        //then
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

        //when
        gameService.updateCategory(100L, 1L);

        //then
    }
}
