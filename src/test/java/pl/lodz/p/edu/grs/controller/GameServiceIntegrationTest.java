package pl.lodz.p.edu.grs.controller;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameServiceIntegrationTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testFindAll() {
        List<Game> all = gameRepository.findAll();
        Assertions.assertThat(all)
                .isNotNull()
                .isEmpty();
    }
}
