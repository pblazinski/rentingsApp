package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.controller.search.SearchDto;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.SearchService;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private final GameRepository gameRepository;

    public SearchServiceImpl(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Page<Game> searchGames(final SearchDto searchDto,
                                  final Long categoryId,
                                  final Pageable pageable) {
        Page<Game> games;

        if (categoryId == null) {
            games = gameRepository.searchGamesWithoutCategory(searchDto, pageable);
        } else {
            games = gameRepository.searchGames(searchDto, categoryId, pageable);
        }

        return games;
    }
}
