package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SearchServiceIT {

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;

    private List<GameDto> games;

    @Before
    public void setUp() throws Exception {
        gameRepository.deleteAll();
        games = StubHelper.stubGames();
    }
}
