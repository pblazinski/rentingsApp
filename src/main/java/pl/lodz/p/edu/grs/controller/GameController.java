package pl.lodz.p.edu.grs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.service.GameService;

@RestController
@RequestMapping(value = "api/games")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<Game> getGames(@RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "0") Integer page) {
        return gameService.findAll(new PageRequest(page, size));
    }

    @PostMapping
    public Game addNewGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameService.getGame(id);
    }

    @PostMapping("/edit")
    public Game updateGame(@RequestBody Game game) {
        return gameService.updateGame(game);
    }

    @PostMapping("/{id}")
    public void removeGame(@PathVariable Long id) {
        gameService.removeGame(id);
    }
}
