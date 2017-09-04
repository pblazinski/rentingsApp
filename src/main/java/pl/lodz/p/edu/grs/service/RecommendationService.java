package pl.lodz.p.edu.grs.service;

import pl.lodz.p.edu.grs.model.game.Game;

import java.util.List;

public interface RecommendationService {

    List<Game> getGameRecommendationBasedOnCategory(long gameId, long limit);

    List<Game> getGameRecommendationBasedOnCollaboration(final Long gamesId, long limit);
}
