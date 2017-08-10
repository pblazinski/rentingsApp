package pl.lodz.p.edu.grs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.service.GameService;

@RestController
@RequestMapping(value = "api/v1/games")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game addNewGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @GetMapping
    public Page<Game> getGames(@RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "0") Integer page) {
        return gameService.findAll(new PageRequest(page, size));
    }

    @GetMapping(value = "/mock")
    public Game mockGames() {
        Game game = new Game("First Game", "I am descriptio", true,80);
        return gameService.addGame(game);
    }

}
