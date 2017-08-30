package pl.lodz.p.edu.grs.controller.borrow;

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
import pl.lodz.p.edu.grs.controller.game.GameDto;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
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
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BorrowGETGetBorrowsEndpointTest {
    @Autowired
    private UserService userService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private GameService gameService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        user = StubHelper.stubAdminUser();
    }

    @Test
    public void shouldReturnPageOfBorrows() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), user.getEmail());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/borrow/")
                .with(user(new AppUser(user)));
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].title").exists())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].title").value(game.getTitle()))
                .andExpect(jsonPath("$.content[0].borrowedGames[0].description").exists())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].description").value(game.getDescription()))
                .andExpect(jsonPath("$.content[0].borrowedGames[0].available").exists())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].available").value(borrow.getBorrowedGames().get(0).isAvailable()))
                .andExpect(jsonPath("$.content[0].borrowedGames[0].price").exists())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].price").value(game.getPrice()))
                .andExpect(jsonPath("$.content[0].borrowedGames[0].category.name").exists())
                .andExpect(jsonPath("$.content[0].borrowedGames[0].category.name").value(game.getCategory().getName()))
                .andExpect(jsonPath("$.content[0].totalPrice").exists())
                .andExpect(jsonPath("$.content[0].totalPrice").value(borrow.getTotalPrice()));
    }
}
