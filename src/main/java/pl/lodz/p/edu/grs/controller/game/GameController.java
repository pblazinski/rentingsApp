package pl.lodz.p.edu.grs.controller.game;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.service.GameService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/games")
@Api(value = "api/games", description = "Endpoints for games management")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    @ApiOperation(value = "Get games pageable")
    public Page<Game> getGames(@PageableDefault final Pageable pageable) {
        return gameService.findAll(pageable);
    }

    @PostMapping
    @ApiOperation(value = "Create game")
    public Game addGame(@RequestBody @Valid final GameDto gameDto) {
        return gameService.addGame(gameDto);
    }

    @PutMapping("/{id}/info")
    public HttpEntity updateTitleAndDescription(@PathVariable final Long id,
                                                @RequestBody @Valid UpdateGameInfoDto updateGameInfoDto){
        Game game = gameService.updateTitleAndDescription(id,
                updateGameInfoDto.getTitle(),
                updateGameInfoDto.getDescription());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/{id}/price")
    public HttpEntity updatePrice(@PathVariable final Long id,
                                  @RequestBody @Valid UpdateGamePriceDto updateGamePriceDto) {
        Game game = gameService.updatePrice(id, updateGamePriceDto.getPrice());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/{id}/available")
    public HttpEntity updateAvailability(@PathVariable final Long id,
                                  @RequestBody @Valid UpdateGameAvailabilityDto updateGameAvailabilityDto) {
        Game game = gameService.updateAvailability(id, updateGameAvailabilityDto.isAvailable());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/category")
    public HttpEntity updateCategory(@RequestBody @Valid final UpdateGameCategoryDto updateGameCategoryDto) {
        Game game = gameService.updateCategory(updateGameCategoryDto.getId(), updateGameCategoryDto.getCategoryId());

        return ResponseEntity.ok(game);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find game by id")
    public Game getGame(@PathVariable final Long id) {
        return gameService.getGame(id);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete game")
    public void remove(@PathVariable final Long id) {
        gameService.remove(id);
    }
}
