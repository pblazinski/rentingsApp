package pl.lodz.p.edu.grs.controller.game;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.RecommendationService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "api/games")
@Api(value = "api/games", description = "Endpoints for games management")
public class GameController {

    private final GameService gameService;
    private final RecommendationService recommendationService;

    //TODO Pre authorize on controllers methods !important
    public GameController(final GameService gameService,
                          final RecommendationService recommendationService) {
        this.gameService = gameService;
        this.recommendationService = recommendationService;
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
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public Game addGame(@RequestBody @Valid final GameDto gameDto) {
        return gameService.addGame(gameDto);
    }

    @PutMapping("/{id}/info")
    @ApiOperation(value = "Update game title and description")
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public HttpEntity updateTitleAndDescription(@PathVariable final Long id,
                                                @RequestBody @Valid UpdateGameInfoDto updateGameInfoDto) {
        Game game = gameService.updateTitleAndDescription(id,
                updateGameInfoDto.getTitle(),
                updateGameInfoDto.getDescription());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/{id}/price")
    @ApiOperation(value = "Update game price")
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public HttpEntity updatePrice(@PathVariable final Long id,
                                  @RequestBody @Valid UpdateGamePriceDto updateGamePriceDto) {
        Game game = gameService.updatePrice(id, updateGamePriceDto.getPrice());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/{id}/available")
    @ApiOperation(value = "Update game availability")
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public HttpEntity updateAvailability(@PathVariable final Long id,
                                         @RequestBody @Valid UpdateGameAvailabilityDto updateGameAvailabilityDto) {
        Game game = gameService.updateAvailability(id, updateGameAvailabilityDto.isAvailable());

        return ResponseEntity.ok(game);
    }

    @PutMapping("/category")
    @ApiOperation(value = "Update game category")
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public HttpEntity updateCategory(@RequestBody @Valid final UpdateGameCategoryDto updateGameCategoryDto) {
        Game game = gameService.updateCategory(updateGameCategoryDto.getId(), updateGameCategoryDto.getCategoryId());

        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/rates")
    @ApiOperation(value = "Add game rate")
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

    @GetMapping("/{gameId}/recommendations")
    @ApiOperation(value = "Get game recomendations")
    public Recommendation getGameRecommendations(@PathVariable final long gameId,
                                                 @RequestParam final long limit) {
        List<Game> basedOnCategory =
                recommendationService.getGameRecommendationBasedOnCategory(gameId, limit);
        List<Game> basedOnCollaboration =
                recommendationService.getGameRecommendationBasedOnCollaboration(gameId, limit);

        return Recommendation.builder()
                .collaborationBased(basedOnCollaboration)
                .categoryBased(basedOnCategory)
                .build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete game")
    @PreAuthorize("hasAuthority('MODIFY_GAME')")
    public void remove(@PathVariable final Long id) {
        gameService.remove(id);
    }
}
