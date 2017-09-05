package pl.lodz.p.edu.grs.controller.user;

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
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.StubHelper;
import pl.lodz.p.edu.grs.util.UserUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserPUTUpdateEmailEndpointTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    private static final String CORRECT_EMAIL = "new@coorect.email";
    private static final String INVALID_EMAIL = "            ";

    private User user;

    @Before
    public void setUp() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        this.user = StubHelper.stubUser();
    }

    @Test
    public void shouldReturnOkStatusWhenUpdateEmail() throws Exception {
        //given
        UpdateUserEmailDto emailDto = mockUpdateEmailDto(CORRECT_EMAIL);

        String content = objectMapper.writeValueAsString(emailDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/email", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());

        assertThat(user.getEmail())
                .isEqualTo(CORRECT_EMAIL);
    }

    @Test
    public void shouldReturnUnauthorizedWhenNotAuthorized() throws Exception {
        //given
        UpdateUserEmailDto emailDto = mockUpdateEmailDto(CORRECT_EMAIL);

        String content = objectMapper.writeValueAsString(emailDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/email", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnForbiddenWhenNotOwner() throws Exception {
        //given
        RegisterUserDto userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setEmail("other@email");
        User user = userService.registerUser(userDTO);

        UpdateUserEmailDto emailDto = mockUpdateEmailDto(CORRECT_EMAIL);

        String content = objectMapper.writeValueAsString(emailDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/email", this.user.getId()))
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
    public void shouldReturnBadRequestWhenUpdateEmailForInvalidEmail() throws Exception {
        //given
        UpdateUserEmailDto emailDto = mockUpdateEmailDto(INVALID_EMAIL);

        String content = objectMapper.writeValueAsString(emailDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/email", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getEmail())
                .isNotSameAs(CORRECT_EMAIL);

        result.andExpect(status().isBadRequest());
    }

    private UpdateUserEmailDto mockUpdateEmailDto(final String email) {
        UpdateUserEmailDto emailDto = new UpdateUserEmailDto();
        emailDto.setEmail(email);
        return emailDto;
    }
}
