package pl.lodz.p.edu.grs.util;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import java.util.Arrays;
import java.util.List;

@Component
public class StubHelper {

    private static UserService userService;
    private static GameService gameService;
    private static CategoryService categoryService;

    public StubHelper(final UserService userService,
                      final GameService gameService,
                      final CategoryService categoryService) {
        StubHelper.userService = userService;
        StubHelper.gameService = gameService;
        StubHelper.categoryService = categoryService;
    }


    public static User stubUser() {
        RegisterUserDto userDTO = UserUtil.mockRegisterUserDTO();
        return userService.registerUser(userDTO);
    }

    public static List<GameDto> stubGames() {
        mockCategories();

        GameDto game = new GameDto("Quake", "FPS bestseller", true, 80, 1L);
        GameDto game1 = new GameDto("H1Z1: King Of The Kill", "Battle Royale", true, 120, 2L);

        gameService.addGame(game);
        gameService.addGame(game1);

        return Arrays.asList(game, game1);
    }

    private static void mockCategories() {
        CategoryDto categoryFPS = new CategoryDto("FPS");
        CategoryDto categoryRPG = new CategoryDto("RPG");

        categoryService.addCategory(categoryFPS);
        categoryService.addCategory(categoryRPG);
    }

}
