package pl.lodz.p.edu.grs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.exceptions.NotFoundException;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;

    private CategoryService categoryService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository,
                           CategoryService categoryService) {
        this.gameRepository = gameRepository;
        this.categoryService = categoryService;
    }

    @Override
    public Page<Game> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game addGame(Game game, Long categoryId) {
        game.setCategory(categoryService.findOne(categoryId));
        return gameRepository.saveAndFlush(game);
    }

    @Override
    public Game updateGame(Game game, Long categoryId, Long gameId) {
        Game result = gameRepository.findOne(gameId);
        if (result == null) {
            throw new NotFoundException(String.format("Game with id=[%d] not found!", game.getId()));
        }
        result.setId(game.getId());
        result.setCategory(categoryService.findOne(categoryId));
        result.setDescription(game.getDescription());
        result.setPrice(game.getPrice());
        result.setTitle(game.getTitle());
        result.setAvailable(game.isAvailable());

        return gameRepository.save(result);
    }

    @Override
    public void deleteGame(Long id) {
        Game result = gameRepository.findOne(id);
        gameRepository.delete(result);
    }

    @Override
    public Game getGame(Long id) {
        return gameRepository.findOne(id);
    }


}
