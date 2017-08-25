package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.factory.GameFactory;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final CategoryService categoryService;

    private final GameFactory gameFactory;

    @Autowired
    public GameServiceImpl(final GameRepository gameRepository,
                           final CategoryService categoryService,
                           final GameFactory gameFactory) {
        this.gameRepository = gameRepository;
        this.categoryService = categoryService;
        this.gameFactory = gameFactory;
    }

    @Override
    public Page<Game> findAll(final Pageable pageable) {
        Page<Game> games = gameRepository.findAll(pageable);

        log.info("Found <{}> users page", games.getTotalElements());

        return games;
    }

    @Override
    public Game addGame(final GameDto game,
                        final Long categoryId) {
        Category category = categoryService.findOne(categoryId);

        Game result = gameFactory.create(game);

        result.updateCategory(category);

        result = gameRepository.save(result);

        log.info("Add game <{}> with title <{}> , price <{}> , availability <{}> and category <{}>",
                result.getId(),
                result.getTitle(),
                result.getPrice(),
                result.isAvailable(),
                result.getCategory().getName());

        return result;
    }

    @Override
    public Game updateTitleAndDescription(final Long id,
                                          final String title,
                                          final String description) {
        Game game = gameRepository.getOne(id);

        game.updateTitleAndDescription(title, description);

        game = gameRepository.save(game);

        log.info("Updated game <{}> with name <{}> and description <{}>",
                game.getId(),
                game.getTitle(),
                game.getDescription());

        return game;
    }

    @Override
    public Game updatePrice(final Long id, final double price) {
        Game game = gameRepository.getOne(id);

        game.updatePrice(price);

        game = gameRepository.save(game);

        log.info("Updated game <{}> with price <{}>",
                game.getId(),
                game.getPrice());

        return game;
    }

    @Override
    public Game updateAvailability(final Long id, final boolean available) {
        Game game = gameRepository.getOne(id);

        game.updateAvailability(available);

        game = gameRepository.save(game);

        log.info("Updated game <{}> with availability <{}>",
                game.getId(),
                game.isAvailable());

        return game;
    }

    @Override
    public Game updateCategory(final Long id, final Long categoryId) {
        Game game = gameRepository.getOne(id);

        Category category = categoryService.findOne(categoryId);

        game.updateCategory(category);

        game = gameRepository.save(game);

        log.info("Updated game <{}> with category <{}>",
                game.getId(),
                game.getCategory().getName());

        return game;
    }

    @Override
    public void remove(Long id) {
        Game game = gameRepository.getOne(id);

        gameRepository.delete(game);

        log.info("Removed game with id <{}> ",
                game.getId());
    }

    @Override
    public Game getGame(Long id) {
        return gameRepository.findOne(id);
    }


}
