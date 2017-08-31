package pl.lodz.p.edu.grs.controller.search;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.service.SearchService;

@RestController
@Api(value = "api/search/games", description = "Endpoints for search management")
@RequestMapping(value = "api/search/games")
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    @ApiOperation("Get users page")
    public Page<Game> searchGames(@PageableDefault final Pageable pageable,
                                  @RequestParam(required = false, defaultValue = "0.0") final Double minPrice,
                                  @RequestParam(required = false, defaultValue = "" + Double.MAX_VALUE + "") final Double maxPrice,
                                  @RequestParam final String title,
                                  @RequestParam(required = false) final Long categoryId) {
        SearchDto searchDto = SearchDto.builder()
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .title(title)
                .build();
        return searchService.searchGames(searchDto, categoryId, pageable);
    }

}
