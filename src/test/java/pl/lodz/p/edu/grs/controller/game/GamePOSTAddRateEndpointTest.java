package pl.lodz.p.edu.grs.controller.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
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
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.game.Game;
import pl.lodz.p.edu.grs.model.game.Rate;
import pl.lodz.p.edu.grs.model.game.RatingSummary;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.util.StubHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GamePOSTAddRateEndpointTest {

    @Autowired
    private GameService gameService;
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

    @Before
    public void setUp() {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldAddRate() throws Exception {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        String content = createRequest(rateDto);

        MockHttpServletRequestBuilder requestBuilder = post(String.format("/api/games/%d/rates", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isOk());

        Game resultGame = gameService.getGame(game.getId());
        RatingSummary ratingSummary = resultGame.getRatingSummary();
        assertThat(ratingSummary)
                .isNotNull();
        assertThat(ratingSummary.getAverage())
                .isNotNull()
                .isEqualTo(rateDto.getRate());
        assertThat(ratingSummary.getRates())
                .isNotEmpty()
                .hasSize(1);
        Rate rate = ratingSummary.getRates().iterator().next();
        assertThat(rate)
                .isNotNull();
        assertThat(rate.getUserId())
                .isNotNull()
                .isEqualTo(user.getId());
        assertThat(rate.getComment())
                .isNotBlank()
                .isEqualTo(rateDto.getComment());
        assertThat(rate.getRate())
                .isNotNull()
                .isEqualTo(rateDto.getRate());
    }

    @Test
    public void shouldReturnBadRequestWhenOneUserTryToAddSecondRate() throws Exception {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        String content = createRequest(rateDto);

        MockHttpServletRequestBuilder requestBuilder = post(String.format("/api/games/%d/rates", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        gameService.addRate(game.getId(), rateDto, user.getEmail());

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenUserTryToAddRateForGameWhichNotBorrow() throws Exception {
        //given
        Borrow borrow = StubHelper.stubBorrow();
        RateDto rateDto = RateDto.builder()
                .rate(10)
                .comment("It's a comment.")
                .build();
        User user = borrow.getUser();
        Game game = borrow.getBorrowedGames().get(0);

        String content = createRequest(rateDto);

        User secondUser = StubHelper.stubSecondUser();

        MockHttpServletRequestBuilder requestBuilder = post(String.format("/api/games/%d/rates", game.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(secondUser)))
                .content(content);

        gameService.addRate(game.getId(), rateDto, user.getEmail());

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isBadRequest());

        Game resultGame = gameService.getGame(game.getId());
        RatingSummary ratingSummary = resultGame.getRatingSummary();
        assertThat(ratingSummary.getRates())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void shouldReturnUnauthorizedWhenTryAddRateByNotLoggedUser() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/api/games/10/rates")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createRequest(new RateDto(10, "it a basic comment")));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldReturnBadRequestWhenTryAddRateWithTooBigRate() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/api/games/10/rates")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createRequest(new RateDto(11, "it a basic comment")));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldReturnBadRequestWhenTryAddRateWithTooSmallRate() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/api/games/10/rates")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createRequest(new RateDto(0, "it a basic comment")));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldReturnBadRequestWhenTryAddRateWithToLongComment() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = post("/api/games/10/rates")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createRequest(new RateDto(1, StringUtils.repeat("a", 256))));

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andDo(print())
                .andExpect(status().isUnauthorized());

    }

    private String createRequest(final RateDto rateDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(rateDto);
    }
}
