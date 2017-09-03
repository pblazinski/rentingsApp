package pl.lodz.p.edu.grs.controller.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.lodz.p.edu.grs.model.game.Game;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Recommendation {

    private List<Game> categoryBased;
    private List<Game> collaborationBased;

    public Recommendation() {
    }
}
