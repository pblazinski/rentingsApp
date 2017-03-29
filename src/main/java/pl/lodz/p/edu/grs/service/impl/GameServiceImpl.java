package pl.lodz.p.edu.grs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.GameService;

@Service
public class GameServiceImpl implements GameService{

    private GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Page<Game> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Game addGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }
}
