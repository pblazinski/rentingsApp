package pl.lodz.p.edu.grs.config;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class DataInitializer {

    private final GameService gameService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BorrowService borrowService;

    public DataInitializer(final GameService gameService,
                           final UserService userService,
                           final CategoryService categoryService,
                           final BorrowService borrowService) {
        this.gameService = gameService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.borrowService = borrowService;
    }


    @PostConstruct
    private void initMocks() {
        mockCategories();
        mockGames();
        mockUsers();
        mockAdminUser();
        mockBorrows();
    }

    private void mockGames() {
        GameDto game = new GameDto("Quake", "FPS bestseller", true, 80, 1L);
        GameDto game1 = new GameDto("H1Z1: King Of The Kill", "Battle Royale", true, 120, 2L);
        GameDto game2 = new GameDto("Destiny 2", "Open Beta", true, 10, 2L);
        GameDto game3 = new GameDto("Empire Earth II", "RPG", true, 41, 2L);
        GameDto game4 = new GameDto("FIFA 18", "Battle Royale", true, 12, 1L);


        gameService.addGame(game);
        gameService.addGame(game1);
        gameService.addGame(game2);
        gameService.addGame(game3);
        gameService.addGame(game4);
    }

    private void mockUsers() {
        RegisterUserDTO firstUser = new RegisterUserDTO("rafal@koper", "rafal", "koper", "password");
        RegisterUserDTO secondUser = new RegisterUserDTO("patryk@blazinski", "Patryk", "blazinski", "password");

        userService.registerUser(firstUser);
        userService.registerUser(secondUser);
    }

    private void mockAdminUser() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO("admin@admin.com", "system", "admin", "password");

        userService.createSystemAdmin(registerUserDTO);
    }

    private void mockCategories() {
        CategoryDto categoryFPS = new CategoryDto("FPS");
        CategoryDto categoryRPG = new CategoryDto("RPG");

        categoryService.addCategory(categoryFPS);
        categoryService.addCategory(categoryRPG);
    }

    private void mockBorrows() {
        BorrowDto borrowDto = new BorrowDto(Arrays.asList(1L, 2L));

        borrowService.addBorrow(borrowDto, "rafal@koper");
    }
}
