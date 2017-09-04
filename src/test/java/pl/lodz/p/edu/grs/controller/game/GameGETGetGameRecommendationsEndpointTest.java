package pl.lodz.p.edu.grs.controller.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GameGETGetGameRecommendationsEndpointTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnOkStatusWhenGetRecommendationForGame() throws Exception {
        //given
        Category rpg = categoryService.addCategory(new CategoryDto("RPG"));
        Category fps = categoryService.addCategory(new CategoryDto("FPS"));

        Game darkSouls = gameService
                .addGame(new GameDto("Dark Souls 3", "Amazing RPG games", true, 56.0, rpg.getId()));
        Game wiedzmin = gameService
                .addGame(new GameDto("Wiedźmin 3", "The best polish rpg games", true, 99.99, rpg.getId()));
        Game horizonZero = gameService
                .addGame(new GameDto("Horizon Zero Down", "opis", true, 91.11, rpg.getId()));
        Game battlefield = gameService
                .addGame(new GameDto("Battlefield", "asdas", true, 99.99, fps.getId()));
        Game cod = gameService
                .addGame(new GameDto("COD", "asdas", true, 99.99, fps.getId()));


        User user = userService
                .registerUser(new RegisterUserDto("test@test", "test", "test", "password"));
        User second = userService
                .registerUser(new RegisterUserDto("second@second", "test", "test", "password"));

        borrowService.addBorrow(new BorrowDto(Arrays.asList(wiedzmin.getId(), darkSouls.getId(), horizonZero.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Collections.singletonList(battlefield.getId())), second.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(wiedzmin.getId(), cod.getId())), user.getEmail());

        MockHttpServletRequestBuilder requestBuilder = get(String.format("/api/games/%d/recommendations?limit=100", wiedzmin.getId()));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryBased.[*].id").exists())
                .andExpect(jsonPath("$.categoryBased.[*].title").exists())
                .andExpect(jsonPath("$.categoryBased.[*].description").exists())
                .andExpect(jsonPath("$.categoryBased.[*].available").exists())
                .andExpect(jsonPath("$.categoryBased.[*].price").exists())
                .andExpect(jsonPath("$.categoryBased.[*].category.id").exists())
                .andExpect(jsonPath("$.categoryBased.[*].category.name").exists())
                .andExpect(jsonPath("$.categoryBased.[*].ratingSummary.average").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].id").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].title").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].description").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].available").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].price").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].category.id").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].category.name").exists())
                .andExpect(jsonPath("$.collaborationBased.[*].ratingSummary.average").exists());

        result.andExpect(jsonPath("$.categoryBased.[*]").isNotEmpty())
                .andExpect(jsonPath("$.categoryBased.[0].id").value(darkSouls.getId()))
                .andExpect(jsonPath("$.categoryBased.[0].title").value(darkSouls.getTitle()))
                .andExpect(jsonPath("$.categoryBased.[0].description").value(darkSouls.getDescription()))
                .andExpect(jsonPath("$.categoryBased.[0].price").value(darkSouls.getPrice()))
                .andExpect(jsonPath("$.categoryBased.[0].category.id").value(darkSouls.getCategory().getId()))
                .andExpect(jsonPath("$.categoryBased.[0].category.name").value(darkSouls.getCategory().getName()))
                .andExpect(jsonPath("$.categoryBased.[0].ratingSummary.average").value(darkSouls.getRatingSummary().getAverage()))
                .andExpect(jsonPath("$.categoryBased.[1].id").value(horizonZero.getId()))
                .andExpect(jsonPath("$.categoryBased.[1].title").value(horizonZero.getTitle()))
                .andExpect(jsonPath("$.categoryBased.[1].description").value(horizonZero.getDescription()))
                .andExpect(jsonPath("$.categoryBased.[1].price").value(horizonZero.getPrice()))
                .andExpect(jsonPath("$.categoryBased.[1].category.id").value(horizonZero.getCategory().getId()))
                .andExpect(jsonPath("$.categoryBased.[1].category.name").value(horizonZero.getCategory().getName()))
                .andExpect(jsonPath("$.categoryBased.[1].ratingSummary.average").value(horizonZero.getRatingSummary().getAverage()))
                .andExpect(jsonPath("$.collaborationBased.[0].id").value(darkSouls.getId()))
                .andExpect(jsonPath("$.collaborationBased.[0].title").value(darkSouls.getTitle()))
                .andExpect(jsonPath("$.collaborationBased.[0].description").value(darkSouls.getDescription()))
                .andExpect(jsonPath("$.collaborationBased.[0].price").value(darkSouls.getPrice()))
                .andExpect(jsonPath("$.collaborationBased.[0].category.id").value(darkSouls.getCategory().getId()))
                .andExpect(jsonPath("$.collaborationBased.[0].category.name").value(darkSouls.getCategory().getName()))
                .andExpect(jsonPath("$.collaborationBased.[0].ratingSummary.average").value(darkSouls.getRatingSummary().getAverage()))
                .andExpect(jsonPath("$.collaborationBased.[1].id").value(horizonZero.getId()))
                .andExpect(jsonPath("$.collaborationBased.[1].title").value(horizonZero.getTitle()))
                .andExpect(jsonPath("$.collaborationBased.[1].description").value(horizonZero.getDescription()))
                .andExpect(jsonPath("$.collaborationBased.[1].price").value(horizonZero.getPrice()))
                .andExpect(jsonPath("$.collaborationBased.[1].category.id").value(horizonZero.getCategory().getId()))
                .andExpect(jsonPath("$.collaborationBased.[1].category.name").value(horizonZero.getCategory().getName()))
                .andExpect(jsonPath("$.collaborationBased.[1].ratingSummary.average").value(horizonZero.getRatingSummary().getAverage()))
                .andExpect(jsonPath("$.collaborationBased.[2].id").value(cod.getId()))
                .andExpect(jsonPath("$.collaborationBased.[2].title").value(cod.getTitle()))
                .andExpect(jsonPath("$.collaborationBased.[2].description").value(cod.getDescription()))
                .andExpect(jsonPath("$.collaborationBased.[2].price").value(cod.getPrice()))
                .andExpect(jsonPath("$.collaborationBased.[2].category.id").value(cod.getCategory().getId()))
                .andExpect(jsonPath("$.collaborationBased.[2].category.name").value(cod.getCategory().getName()))
                .andExpect(jsonPath("$.collaborationBased.[2].ratingSummary.average").value(cod.getRatingSummary().getAverage()));
    }
}
