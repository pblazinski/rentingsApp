package pl.lodz.p.edu.grs.controller.search;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.service.SearchService;

@RestController
@Api(value = "api/searches", description = "Endpoints for user management")
@RequestMapping(value = "api/search/games")
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    @ApiOperation("Get users page")
    public Page<Game> getUsers(@PageableDefault final Pageable pageable,
                               final SearchDto searchDto) {
        return searchService.searchGames(searchDto, pageable);
    }

}
