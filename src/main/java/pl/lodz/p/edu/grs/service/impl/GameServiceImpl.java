package pl.lodz.p.edu.grs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.factory.GameFactory;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.game.Rate;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Slf4j
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final BorrowRepository borrowRepository;

    private final GameRepository gameRepository;

    private final CategoryService categoryService;

    private final GameFactory gameFactory;

    @Autowired
    public GameServiceImpl(final GameRepository gameRepository,
                           final CategoryService categoryService,
                           final GameFactory gameFactory,
                           final BorrowRepository borrowRepository) {
        this.gameRepository = gameRepository;
        this.categoryService = categoryService;
        this.gameFactory = gameFactory;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public List<Game> getMostPopular(final Long amount) {
        List<Borrow> borrows = borrowRepository.findAll();
        List<Long> gamesIds = new ArrayList<>();
        borrows.forEach(borrow -> borrow.getBorrowedGames().forEach(game -> gamesIds.add(game.getId())));


        return gamesIds.stream()
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(amount)
                .map(Map.Entry::getKey)
                .map(gameRepository::getOne)
                .collect(toList());
    }

    @Override
    public Page<Game> findAll(final Pageable pageable) {
        Page<Game> games = gameRepository.findAll(pageable);

        log.info("Found <{}> users page", games.getTotalElements());

        return games;
    }

    @Override
    public Game addGame(final GameDto game) {
        Category category = categoryService.findOne(game.getCategoryId());

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
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException(); //TODO add tests veryfing that
        }
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
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
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
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
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
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
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
    public void remove(final Long id) {
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
        Game game = gameRepository.getOne(id);

        gameRepository.delete(game);

        log.info("Removed game with id <{}> ",
                game.getId());
    }

    @Override
    public Game getGame(final Long id) {
        if (!gameRepository.exists(id)) {
            throw new EntityNotFoundException();
        }
        Game game = gameRepository.findOne(id);

        log.info("Got game with id <{}> ", game.getId());

        return gameRepository.findOne(id);
    }


}
