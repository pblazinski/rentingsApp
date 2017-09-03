package pl.lodz.p.edu.grs.controller.game;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.service.GameService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "api/games")
@Api(value = "api/games", description = "Endpoints for games management")
public class GameController {

    private final GameService gameService;

    private final Validator category;

    //TODO Pre authorize on controllers methods !important
    @Autowired
    public GameController(final GameService gameService,
                          @Qualifier("categoryValidator") final Validator category) {
        this.gameService = gameService;
        this.category = category;
    }

    @InitBinder("category")
    protected void initRegistrationApplicationCreateRequestBinder(final WebDataBinder binder) {
        binder.setValidator(category);
    }

    @GetMapping
    @ApiOperation(value = "Get games pageable")
    public Page<Game> getGames(@PageableDefault final Pageable pageable) {
        return gameService.findAll(pageable);
    }


    @GetMapping("/popular/{amount}")
    @ApiOperation(value = "Get most popular games")
    public List<Game> getGames(@PathVariable Long amount) {
        return gameService.getMostPopular(amount);
    }

    @PostMapping
    @ApiOperation(value = "Create game")
    public Game addGame(@RequestBody @Valid final GameDto gameDto) {
        return gameService.addGame(gameDto);
    }

    @PutMapping("/{id}/info")
    public HttpEntity updateTitleAndDescription(@PathVariable final Long id,
                                                @RequestBody @Valid UpdateGameInfoDto updateGameInfoDto) {
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

    @PostMapping("/{gameId}/rates")
    public HttpEntity addRate(@PathVariable final long gameId,
                              @RequestBody @Valid RateDto rateDto,
                              final Principal principal) {
        Game game = gameService.addRate(gameId, rateDto, principal.getName());

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
