package pl.lodz.p.edu.grs.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.GameRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GameServiceIT {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void shouldReturnEmptyListOfGame() {
        List<Game> all = gameRepository.findAll();

        assertThat(all)
                .isNotNull()
                .isEmpty();
    }
}
