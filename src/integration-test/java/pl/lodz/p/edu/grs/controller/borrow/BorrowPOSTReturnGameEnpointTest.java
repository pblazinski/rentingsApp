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

import java.util.Collections;
import java.util.Iterator;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BorrowPOSTReturnGameEnpointTest {
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
    public void shouldReturnAllGamesAndSetReturnedTime() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), user.getEmail());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(String.format("/api/borrow/%d", borrow.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        String body = result.andReturn().getResponse().getContentAsString();
        long id = getIdFromContentBodyJson(body);
        Borrow borrowed = borrowRepository.findOne(id);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowedGames[0].title").exists())
                .andExpect(jsonPath("$.borrowedGames[0].title").value(game.getTitle()))
                .andExpect(jsonPath("$..borrowedGames[0].description").exists())
                .andExpect(jsonPath("$.borrowedGames[0].description").value(game.getDescription()))
                .andExpect(jsonPath("$.borrowedGames[0].available").exists())
                .andExpect(jsonPath("$.borrowedGames[0].available").value(borrowed.getBorrowedGames().get(0).isAvailable()))
                .andExpect(jsonPath("$.borrowedGames[0].price").exists())
                .andExpect(jsonPath("$.borrowedGames[0].price").value(game.getPrice()))
                .andExpect(jsonPath("$.borrowedGames[0].category.name").exists())
                .andExpect(jsonPath("$.borrowedGames[0].category.name").value(game.getCategory().getName()))
                .andExpect(jsonPath("$.timeBack").exists())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$.totalPrice").value(borrowed.getTotalPrice()));
    }

    @Test
    public void shouldReturnUnauthorizedStatusWhenReturnAllGamesAndSetReturnedTime() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());
        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());

        Game game = gameService.addGame(gameDto);

        Borrow borrow = borrowService.addBorrow(new BorrowDto(Collections.singletonList(game.getId())), user.getEmail());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(String.format("/api/borrow/%d", borrow.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

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
