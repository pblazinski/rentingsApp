package pl.lodz.p.edu.grs.controller.game;

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
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import java.util.Iterator;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GamePOSTAddGameEndpointTest {

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
    private ObjectMapper objectMapper;
    @Autowired
    private BorrowRepository borrowRepository;

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
    public void shouldReturnOkStatusWhenAddGame() throws Exception {
        //given
        GameDto gameDto = GameUtil.mockGameDto();

        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        gameDto.setCategoryId(category.getId());

        String content = objectMapper.writeValueAsString(gameDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/games/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        String body = result.andReturn().getResponse().getContentAsString();
        long id = getIdFromContentBodyJson(body);
        Game game = gameRepository.findOne(id);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(game.getId()))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").value(game.getTitle()))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(game.getDescription()))
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.price").value(game.getPrice()))
                .andExpect(jsonPath("$.category.id").exists())
                .andExpect(jsonPath("$.category.id").value(game.getCategory().getId()))
                .andExpect(jsonPath("$.category.name").exists())
                .andExpect(jsonPath("$.category.name").value(game.getCategory().getName()))
                .andExpect(jsonPath("$.available").exists())
                .andExpect(jsonPath("$.available").value(game.isAvailable()));

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
