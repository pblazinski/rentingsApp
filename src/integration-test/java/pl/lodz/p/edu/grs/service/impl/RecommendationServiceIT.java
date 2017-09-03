package pl.lodz.p.edu.grs.service.impl;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
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
import pl.lodz.p.edu.grs.service.RecommendationService;
import pl.lodz.p.edu.grs.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RecommendationServiceIT {

    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private RecommendationService recommendationService;

    @After
    public void tearDown() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnGamesListBasedOnCategory() {
        //given
        Category rpg = categoryService.addCategory(new CategoryDto("RPG"));
        Category fps = categoryService.addCategory(new CategoryDto("FPS"));

        Game darkSouls = gameService
                .addGame(new GameDto("Dark Souls 3", "Amazing RPG games", true, 56.0, rpg.getId()));
        Game wiedzmin = gameService
                .addGame(new GameDto("Wiedźmin 3", "The best polish rpg games", true, 99.99, rpg.getId()));
        Game horizonZero = gameService
                .addGame(new GameDto("Horizon Zero Down", "opis", true, 91.11, rpg.getId()));
        Game battlefield = gameService
                .addGame(new GameDto("Battlefield", "asdas", true, 99.99, fps.getId()));


        User user = userService
                .registerUser(new RegisterUserDto("test@test", "test", "test", "password"));
        User second = userService
                .registerUser(new RegisterUserDto("second@second", "test", "test", "password"));

        borrowService.addBorrow(new BorrowDto(Collections.singletonList(wiedzmin.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Collections.singletonList(battlefield.getId())), second.getEmail());

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCategory(darkSouls.getId(), 100);

        //then

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(wiedzmin)
                .doesNotContain(darkSouls, horizonZero, battlefield);

        Game game = result.get(0);

        assertThat(game.getTitle())
                .isEqualTo(wiedzmin.getTitle());
    }

    @Test
    public void shouldReturnOneGameWhenSpecifyLimitToOneBasedOnCategory() {
        //given
        Category rpg = categoryService.addCategory(new CategoryDto("RPG"));
        Category fps = categoryService.addCategory(new CategoryDto("FPS"));

        Game darkSouls = gameService
                .addGame(new GameDto("Dark Souls 3", "Amazing RPG games", true, 56.0, rpg.getId()));
        Game wiedzmin = gameService
                .addGame(new GameDto("Wiedźmin 3", "The best polish rpg games", true, 99.99, rpg.getId()));
        Game horizonZero = gameService
                .addGame(new GameDto("Horizon Zero Down", "opis", true, 91.11, rpg.getId()));
        Game battlefield = gameService
                .addGame(new GameDto("Battlefield", "asdas", true, 99.99, fps.getId()));


        User user = userService
                .registerUser(new RegisterUserDto("test@test", "test", "test", "password"));
        User second = userService
                .registerUser(new RegisterUserDto("second@second", "test", "test", "password"));

        borrowService.addBorrow(new BorrowDto(Arrays.asList(wiedzmin.getId(), horizonZero.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Collections.singletonList(battlefield.getId())), second.getEmail());

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCategory(darkSouls.getId(), 1);

        //then

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .doesNotContain(darkSouls, battlefield);
    }

    @Test
    public void shouldReturnEmptyGamesListWhenNotFound() {
        //given
        Category rpg = categoryService.addCategory(new CategoryDto("RPG"));
        Category fps = categoryService.addCategory(new CategoryDto("FPS"));

        Game darkSouls = gameService
                .addGame(new GameDto("Dark Souls 3", "Amazing RPG games", true, 56.0, rpg.getId()));
        Game wiedzmin = gameService
                .addGame(new GameDto("Wiedźmin 3", "The best polish rpg games", true, 99.99, rpg.getId()));
        Game horizonZero = gameService
                .addGame(new GameDto("Horizon Zero Down", "opis", true, 91.11, rpg.getId()));
        Game battlefield = gameService
                .addGame(new GameDto("Battlefield", "asdas", true, 99.99, fps.getId()));


        User user = userService
                .registerUser(new RegisterUserDto("test@test", "test", "test", "password"));
        User second = userService
                .registerUser(new RegisterUserDto("second@second", "test", "test", "password"));

        borrowService.addBorrow(new BorrowDto(Collections.singletonList(wiedzmin.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Collections.singletonList(battlefield.getId())), second.getEmail());

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCategory(battlefield.getId(), 100);

        //then
        assertThat(result)
                .isNotNull()
                .hasSize(0)
                .doesNotContain(darkSouls, wiedzmin, horizonZero, battlefield);
    }

    @Test
    public void shouldReturnGamesListBasedOnCollaborationRecommendation() {
        //given
        Category rpg = categoryService.addCategory(new CategoryDto("RPG"));
        Category fps = categoryService.addCategory(new CategoryDto("FPS"));

        Game darkSouls = gameService
                .addGame(new GameDto("Dark Souls 3", "Amazing RPG games", true, 56.0, rpg.getId()));
        Game wiedzmin = gameService
                .addGame(new GameDto("Wiedźmin 3", "The best polish rpg games", true, 99.99, rpg.getId()));
        Game horizonZero = gameService
                .addGame(new GameDto("Horizon Zero Down", "opis", true, 91.11, rpg.getId()));
        Game battlefield = gameService
                .addGame(new GameDto("Battlefield", "asdas", true, 99.99, fps.getId()));
        Game cod = gameService
                .addGame(new GameDto("COD", "asdas", true, 99.99, fps.getId()));


        User user = userService
                .registerUser(new RegisterUserDto("test@test", "test", "test", "password"));
        User second = userService
                .registerUser(new RegisterUserDto("second@second", "test", "test", "password"));

        borrowService.addBorrow(new BorrowDto(Arrays.asList(wiedzmin.getId(), darkSouls.getId(), horizonZero.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Collections.singletonList(battlefield.getId())), second.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(wiedzmin.getId(), cod.getId())), user.getEmail());

        //when
        List<Game> result = recommendationService.getGameRecommendationBasedOnCollaboration(wiedzmin.getId(), 100);

        //then
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .contains(darkSouls, horizonZero, cod)
                .doesNotContain(battlefield, wiedzmin);
    }
}
