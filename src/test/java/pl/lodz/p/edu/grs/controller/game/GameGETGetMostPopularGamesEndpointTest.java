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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.edu.grs.controller.borrow.BorrowDto;
import pl.lodz.p.edu.grs.controller.category.CategoryDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GameGETGetMostPopularGamesEndpointTest {
    @Autowired
    private GameService gameService;
    @Autowired
    private CategoryService categoryService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private BorrowService borrowService;

    private User user;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = StubHelper.stubUser();
    }


    @Test
    public void shouldReturn4MostPopularGames() throws Exception {
        //given
        Category category = categoryService.addCategory(new CategoryDto("1"));

        Game game = gameService.addGame(new GameDto("1", "1", true, 10, category.getId()));
        Game game1 = gameService.addGame(new GameDto("2", "1", true, 10, category.getId()));
        Game game2 = gameService.addGame(new GameDto("3", "1", true, 10, category.getId()));
        Game game3 = gameService.addGame(new GameDto("4", "1", true, 10, category.getId()));
        Game game4 = gameService.addGame(new GameDto("5", "1", true, 10, category.getId()));

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId(), game1.getId(), game2.getId())), user.getEmail());
        Borrow borrow2 = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId(), game1.getId())), user.getEmail());
        Borrow borrow3 = borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId())), user.getEmail());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/games/popular/3")
                .with(user(new AppUser(user)));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].title").value(game4.getTitle()))
                .andExpect(jsonPath("$.[1].title").exists())
                .andExpect(jsonPath("$.[1].title").value(game1.getTitle()))
                .andExpect(jsonPath("$.[2].title").exists())
                .andExpect(jsonPath("$.[2].title").value(game2.getTitle()));

    }
}
