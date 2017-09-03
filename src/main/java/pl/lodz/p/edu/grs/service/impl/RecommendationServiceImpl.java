package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.RecommendationService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final GameRepository gameRepository;
    private final BorrowRepository borrowRepository;

    public RecommendationServiceImpl(final GameRepository gameRepository,
                                     final BorrowRepository borrowRepository) {
        this.gameRepository = gameRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public List<Game> getGameRecommendationBasedOnCategory(final long gameId, final long limit) {
        List<Game> games = gameRepository.findAllWithoutSelectWithCategorySameAsSelectedGame(gameId);
        List<Borrow> borrows = borrowRepository.findAllByBorrowedGamesIn(games);

        List<Game> recommendation = borrows.stream()
                .flatMap(borrow -> borrow.getBorrowedGames().stream())
                .filter(game -> anyMatch(game, games))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());

        log.info("Found <{}> game with same category as <{}> which was borrow.", recommendation.size(), gameId);

        return recommendation;
    }

    private boolean anyMatch(final Game game, final List<Game> games) {
        return games.stream()
                .filter(g -> Objects.equals(g.getId(), game.getId()))
                .count() == 1;
    }
}
