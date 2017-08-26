package pl.lodz.p.edu.grs.controller.game;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.CategoryUtil;
import pl.lodz.p.edu.grs.util.GameUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GamePUTUpdatePriceEndpointTest {

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

    @Autowired
    private ObjectMapper objectMapper;

    public static final double CORRECT_PRICE = 10.99;

    public static final double BELOWZERO_PRICE = -5;

    @Before
    public void setUp() {
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    public void shouldReturnOkStatusWhenUpdateGamePrice() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGamePriceDto gameInfoDto = new UpdateGamePriceDto(CORRECT_PRICE);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/price", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        game = gameRepository.findOne(game.getId());

        assertThat(game.getPrice())
                .isEqualTo(CORRECT_PRICE);

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

    @Test
    public void shouldBadRequestStatysWhenUpdateBelovZeroPrice() throws Exception {
        //given
        Category category = categoryService.addCategory(CategoryUtil.mockCategoryDto());

        GameDto gameDto = GameUtil.mockGameDto();
        gameDto.setCategoryId(category.getId());
        UpdateGamePriceDto gameInfoDto = new UpdateGamePriceDto(BELOWZERO_PRICE);
        Game game = gameService.addGame(gameDto);

        String content = objectMapper.writeValueAsString(gameInfoDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(String.format("/api/games/%d/price", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        game = gameRepository.findOne(game.getId());

        assertThat(game.getPrice())
                .isNotSameAs(BELOWZERO_PRICE);

        result.andExpect(status().isBadRequest());
    }
}
