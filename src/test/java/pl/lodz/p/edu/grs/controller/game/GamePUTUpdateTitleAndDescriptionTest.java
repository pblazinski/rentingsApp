package pl.lodz.p.edu.grs.controller.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.edu.grs.model.Category;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;
import pl.lodz.p.edu.grs.util.StubHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GamePUTUpdateTitleAndDescriptionTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BorrowRepository borrowRepository;

    private User user;

    private User admin;

    private static final String CORRECT_TITLE = "Title";

    private static final String CORRECT_DESCRIPTION = "Description";

    private static final String BLANK_VALUE = "   ";

    @Before
    public void setUp() {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        admin = StubHelper.stubSystemAdmin();
        user = StubHelper.stubUser();
    }

    @Test
    public void shouldReturnOkStatusWhenUpdateTitleAndDescription() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGameInfoDto gameInfoDto = new UpdateGameInfoDto(CORRECT_TITLE, CORRECT_DESCRIPTION);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/info", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(admin)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        game = gameRepository.findOne(game.getId());

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

        assertThat(game.getDescription())
                .isEqualTo(CORRECT_DESCRIPTION);
        assertThat(game.getTitle())
                .isEqualTo(CORRECT_TITLE);
    }

    @Test
    public void shouldReturnBadReuestWhenUpdateTitleAndDescriptionBlank() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGameInfoDto gameInfoDto = new UpdateGameInfoDto(BLANK_VALUE, BLANK_VALUE);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/info", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        game = gameRepository.findOne(game.getId());

        assertThat(game.getDescription())
                .isNotSameAs(BLANK_VALUE);
        assertThat(game.getTitle())
                .isNotSameAs(BLANK_VALUE);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnForbiddenStatusWhenUpdateTitleAndDescription() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGameInfoDto gameInfoDto = new UpdateGameInfoDto(CORRECT_TITLE, CORRECT_DESCRIPTION);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/info", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnUnauthorizedStatusWhenUpdateTitleAndDescription() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGameInfoDto gameInfoDto = new UpdateGameInfoDto(CORRECT_TITLE, CORRECT_DESCRIPTION);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/info", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isUnauthorized());
    }
}
