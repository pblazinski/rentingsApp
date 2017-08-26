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
import pl.lodz.p.edu.grs.model.user.User;
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
public class UserPUTUpdatePasswordEndpointTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String NEW_PASSWORD = "new password";

    private static final String INVALID_PASSWORD = "            ";

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnOkStatusWhenUpdatePassword() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        String oldPassword = user.getPassword();
        UpdateUserPasswordDto passwordDto = mockUpdatePasswordDto(NEW_PASSWORD);

        String content = objectMapper.writeValueAsString(passwordDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/password", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getPassword())
                .isNotSameAs(oldPassword);

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
    public void shouldReturnBadRequestWhenUpdatePasswordForBlankPassword() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        String oldPassword = user.getPassword();
        UpdateUserPasswordDto emailDto = mockUpdatePasswordDto(INVALID_PASSWORD);

        String content = objectMapper.writeValueAsString(emailDto);

        MockHttpServletRequestBuilder requestBuilder = put(String.format("/api/users/%d/password", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        user = userRepository.findOne(user.getId());

        assertThat(user.getPassword())
                .isEqualTo(oldPassword);

        result.andExpect(status().isBadRequest());
    }

    private UpdateUserPasswordDto mockUpdatePasswordDto(final String password) {
        UpdateUserPasswordDto passwordDto = new UpdateUserPasswordDto();
        passwordDto.setPassword(password);
        return passwordDto;
    }
}
