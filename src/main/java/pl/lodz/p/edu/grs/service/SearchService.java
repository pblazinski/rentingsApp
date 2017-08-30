package pl.lodz.p.edu.grs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.edu.grs.controller.search.SearchDto;
import pl.lodz.p.edu.grs.model.Game;

public interface SearchService {

    Page<Game> searchGames(SearchDto searchDto, final Long categoryId, Pageable pageable);
}
