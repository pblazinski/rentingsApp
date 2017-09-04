package pl.lodz.p.edu.grs.controller.borrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.edu.grs.controller.game.GameDto;
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
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.Arrays;
import java.util.Iterator;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BorrowPOSTAddBorrowEnpointTest {
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
    @Autowired
    private ObjectMapper objectMapper;

    private static final double DISCOUNT = 0.9;

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
    public void shouldAddNewBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        String content = "   {\n" +
                "   \t\"games\": [" + game.getId() + "]\n" +
                "   }";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/borrow/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        String body = result.andReturn().getResponse().getContentAsString();
        long id = getIdFromContentBodyJson(body);
        Borrow borrow = borrowRepository.findOne(id);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowedGames[0].title").exists())
                .andExpect(jsonPath("$.borrowedGames[0].title").value(game.getTitle()))
                .andExpect(jsonPath("$..borrowedGames[0].description").exists())
                .andExpect(jsonPath("$.borrowedGames[0].description").value(game.getDescription()))
                .andExpect(jsonPath("$.borrowedGames[0].available").exists())
                .andExpect(jsonPath("$.borrowedGames[0].available").value(borrow.getBorrowedGames().get(0).isAvailable()))
                .andExpect(jsonPath("$.borrowedGames[0].price").exists())
                .andExpect(jsonPath("$.borrowedGames[0].price").value(game.getPrice()))
                .andExpect(jsonPath("$.borrowedGames[0].category.name").exists())
                .andExpect(jsonPath("$.borrowedGames[0].category.name").value(game.getCategory().getName()))
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$.totalPrice").value(borrow.getTotalPrice()));
    }

    @Test
    public void shouldAddNewBorrowAndCountDiscount() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);
        Game game1 = gameService.addGame(new GameDto("1","1",true,20.99,category.getId()));
        Game game2 = gameService.addGame(new GameDto("2","1",true,20.99,category.getId()));
        Game game3 = gameService.addGame(new GameDto("3","1",true,20.99,category.getId()));
        Game game4 = gameService.addGame(new GameDto("4","1",true,20.99,category.getId()));
        Game game5 = gameService.addGame(new GameDto("5","1",true,20.99,category.getId()));
        Game game6 = gameService.addGame(new GameDto("6","1",true,20.99,category.getId()));
        Game game7 = gameService.addGame(new GameDto("7","1",true,20.99,category.getId()));
        Game game8 = gameService.addGame(new GameDto("8","1",true,20.99,category.getId()));
        Game game9 = gameService.addGame(new GameDto("9","1",true,20.99,category.getId()));
        Game game10 = gameService.addGame(new GameDto("10","1",true,20.99,category.getId()));

        borrowService.addBorrow(new BorrowDto(Arrays.asList(game.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game1.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game2.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game3.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game4.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game5.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game6.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game7.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game8.getId())), user.getEmail());
        borrowService.addBorrow(new BorrowDto(Arrays.asList(game9.getId())), user.getEmail());

        String content = "   {\n" +
                "   \t\"games\": [" + game10.getId() + "]\n" +
                "   }";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/borrow/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        String body = result.andReturn().getResponse().getContentAsString();
        long id = getIdFromContentBodyJson(body);
        Borrow borrow = borrowRepository.findOne(id);

        //then


        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowedGames[0].title").exists())
                .andExpect(jsonPath("$.borrowedGames[0].title").value(game10.getTitle()))
                .andExpect(jsonPath("$..borrowedGames[0].description").exists())
                .andExpect(jsonPath("$.borrowedGames[0].description").value(game10.getDescription()))
                .andExpect(jsonPath("$.borrowedGames[0].available").exists())
                .andExpect(jsonPath("$.borrowedGames[0].available").value(borrow.getBorrowedGames().get(0).isAvailable()))
                .andExpect(jsonPath("$.borrowedGames[0].price").exists())
                .andExpect(jsonPath("$.borrowedGames[0].price").value(game10.getPrice()))
                .andExpect(jsonPath("$.borrowedGames[0].category.name").exists())
                .andExpect(jsonPath("$.borrowedGames[0].category.name").value(game10.getCategory().getName()))
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$.totalPrice").value(game10.getPrice() * DISCOUNT));
    }

    @Test
    public void shouldReturnUnauthorizedWhenAddNewBorrow() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        String content = "   {\n" +
                "   \t\"games\": [" + game.getId() + "]\n" +
                "   }";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/borrow/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);
        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isUnauthorized());
    }



    private long getIdFromContentBodyJson(final String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);

        Iterator<?> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals("id")) {
                return (Integer) jsonObject.get(key);
            }
        }
        return 1L;
    }
}
