package pl.lodz.p.edu.grs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    GameService gameService;

    UserService userService;

    CategoryService categoryService;

    @Autowired
    public DataInitializer(final GameService gameService,
                           final UserService userService,
                           final CategoryService categoryService) {
        this.gameService = gameService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @PostConstruct
    private void initMocks() {
        mockCategories();
        mockGames();
        mockUsers();
    }

    private void mockGames() {
        GameDto game = new GameDto("Quake", "FPS bestseller", true, 80);
        GameDto game1 = new GameDto("H1Z1: King Of The Kill", "Battle Royale", true, 120);

        gameService.addGame(game, 1L);
        gameService.addGame(game1, 2L);
    }

    private void mockUsers() {
        RegisterUserDTO firstUser = new RegisterUserDTO("rafal@koper", "rafal", "koper", "password");
        RegisterUserDTO secondUser = new RegisterUserDTO("patryk@blazinski", "Patryk", "blazinski", "password");

        userService.registerUser(firstUser);
        userService.registerUser(secondUser);
    }

    private void mockCategories() {
        CategoryDto categoryFPS = new CategoryDto("FPS");
        CategoryDto categoryRPG = new CategoryDto("RPG");

        categoryService.addCategory(categoryFPS);
        categoryService.addCategory(categoryRPG);
    }
}
