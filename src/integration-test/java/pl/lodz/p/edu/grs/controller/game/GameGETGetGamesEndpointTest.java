package pl.lodz.p.edu.grs.controller.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameGETGetGamesEndpointTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
    gameRepository.deleteAll();
    categoryRepository.deleteAll();
    }

    @Test
    public void shouldReturnPageOfGames() throws Exception {
        CategoryDto categoryDto = CategoryUtil.mockCategoryDto();
        Category category = categoryService.addCategory(categoryDto);

        GameDto gameDto = GameUtil.mockGameDto();
        Game game = gameService.addGame(gameDto, category.getId());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/games/");

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].title").value(game.getTitle()))
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].description").value(game.getDescription()))
                .andExpect(jsonPath("$.content[0].price").exists())
                .andExpect(jsonPath("$.content[0].price").value(game.getPrice()))
                .andExpect(jsonPath("$.content[0].category.id").exists())
                .andExpect(jsonPath("$.content[0].category.id").value(game.getCategory().getId()))
                .andExpect(jsonPath("$.content[0].category.name").exists())
                .andExpect(jsonPath("$.content[0].category.name").value(game.getCategory().getName()))
                .andExpect(jsonPath("$.content[0].available").exists())
                .andExpect(jsonPath("$.content[0].available").value(game.isAvailable()));


    }

}
