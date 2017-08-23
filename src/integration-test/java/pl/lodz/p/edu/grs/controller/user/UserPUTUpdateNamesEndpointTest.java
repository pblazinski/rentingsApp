package pl.lodz.p.edu.grs.controller.user;

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
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.UserUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserPUTUpdateNamesEndpointTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CORRECT_FIRST_NAME = "Correct First Name";

    private static final String CORRECT_LAST_NAME = "Correct Last name";

    private static final String BLANK_VALUE = "            ";

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnOkStatusWhenUpdateNames() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        UpdateUserNamesDto namesDto = mockUpdateUserNamesDto(CORRECT_FIRST_NAME, CORRECT_LAST_NAME);

        String content = objectMapper.writeValueAsString(namesDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/names", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getFirstName())
                .isEqualTo(CORRECT_FIRST_NAME);

        assertThat(user.getLastName())
                .isEqualTo(CORRECT_LAST_NAME);

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
    }

    @Test
    public void shouldReturnBadRequestWhenUpdateNamesWithFirstNameIsBlank() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        UpdateUserNamesDto namesDto = mockUpdateUserNamesDto(BLANK_VALUE, CORRECT_LAST_NAME);

        String content = objectMapper.writeValueAsString(namesDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/names", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getFirstName())
                .isNotSameAs(CORRECT_FIRST_NAME);

        assertThat(user.getLastName())
                .isNotSameAs(CORRECT_LAST_NAME);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenUpdateNamesWithLastNameIsBlank() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        UpdateUserNamesDto namesDto = mockUpdateUserNamesDto(CORRECT_FIRST_NAME, BLANK_VALUE);

        String content = objectMapper.writeValueAsString(namesDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/names", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getFirstName())
                .isNotSameAs(CORRECT_FIRST_NAME);

        assertThat(user.getLastName())
                .isNotSameAs(CORRECT_LAST_NAME);

        result.andExpect(status().isBadRequest());
    }

    private UpdateUserNamesDto mockUpdateUserNamesDto(final String firstName, final String lastName) {
        UpdateUserNamesDto namesDto = new UpdateUserNamesDto();
        namesDto.setFirstName(firstName);
        namesDto.setLastName(lastName);
        return namesDto;
    }
}
