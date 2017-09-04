package pl.lodz.p.edu.grs.util;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class StubHelper {

    private static UserService userService;
    private static GameService gameService;
    private static CategoryService categoryService;
    private static BorrowService borrowService;

    private static List<Category> categories;

    public StubHelper(final UserService userService,
                      final GameService gameService,
                      final CategoryService categoryService,
                      final BorrowService borrowService) {
        StubHelper.userService = userService;
        StubHelper.gameService = gameService;
        StubHelper.categoryService = categoryService;
        StubHelper.borrowService = borrowService;
    }


    public static User stubUser() {
        RegisterUserDto userDTO = UserUtil.mockRegisterUserDTO();
        return userService.registerUser(userDTO);
    }

    public static User stubSecondUser() {
        RegisterUserDto userDto = UserUtil.mockRegisterUserDTO();
        userDto.setEmail(UserUtil.EMAIL2);
        return userService.registerUser(userDto);
    }

    public static User stubSystemAdmin() {
        RegisterUserDto adminDto = new RegisterUserDto("email@admin.pl", "THE BEST", "ADMIN", "password");
        return userService.createSystemAdmin(adminDto);
    }

    public static List<Game> stubGames() {
        GameDto game = new GameDto("Quake", "FPS bestseller", true, 80, categories.get(0).getId());
        GameDto game1 = new GameDto("H1Z1: King Of The Kill", "Battle Royale", true, 120, categories.get(1).getId());

        List<Game> games = new ArrayList<>();

        games.add(gameService.addGame(game));
        games.add(gameService.addGame(game1));

        return games;
    }

    public static List<Category> stubCategories() {
        CategoryDto categoryFPS = new CategoryDto("FPS");
        CategoryDto categoryRPG = new CategoryDto("RPG");

        Category category = categoryService.addCategory(categoryFPS);
        Category category1 = categoryService.addCategory(categoryRPG);

        categories = Arrays.asList(category, category1);
        return categories;
    }

    public static Borrow stubBorrow() {
        User user = stubUser();
        stubCategories();
        List<Game> games = stubGames();

        BorrowDto borrowDto = new BorrowDto(Collections.singletonList(games.get(0).getId()));
        return borrowService.addBorrow(borrowDto, user.getEmail());
    }

}
