package pl.lodz.p.edu.grs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    GameService gameService;

    UserService userService;

    @Autowired
    public DataInitializer(final GameService gameService,
                           final UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }


    @PostConstruct
    private void initMocks() {
        mockGames();
        mockUsers();
    }

    private void mockGames() {
        Game game = new Game("Quake", "FPS bestseller", true, 80);
        Game game1 = new Game("H1Z1: King Of The Kill", "Battle Royale", true, 120);

        gameService.addGame(game);
        gameService.addGame(game1);
    }

    private void mockUsers() {
        User user = new User("Ralink", "rafal14kop@gmail.com", true);
        User user1 = new User("Patryk", "patryk@blazinski.com", true);

        userService.addUser(user);
        userService.addUser(user1);
    }


}
