package pl.lodz.p.edu.grs.controller.user;

import com.fasterxml.jackson.core.type.TypeReference;
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
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.util.UserUtil;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserPOSTRegisterUserEndpointTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BLANK_VALUE = "            ";

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnOkStatusWhenRegisterUser() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();

        String content = objectMapper.writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        String body = result.andReturn().getResponse().getContentAsString();
        long id = getIdFromContentBody(body);
        User user = userRepository.findOne(id);

        result.andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void shouldReturnBadRequestWhenRegisterUserWithInvalidEmail() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setEmail("eee");

        String content = objectMapper.writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenRegisterUserWithBlankFirstName() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setFirstName(BLANK_VALUE);

        String content = objectMapper.writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenRegisterUserWithBlankLastName() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setLastName(BLANK_VALUE);

        String content = objectMapper.writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenRegisterUserWithBlankPassword() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setPassword(BLANK_VALUE);

        String content = objectMapper.writeValueAsString(userDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isBadRequest());
    }

    private long getIdFromContentBody(final String content) throws IOException {
        TypeReference<User> typeRef
                = new TypeReference<User>() {
        };

        User user = objectMapper.readValue(content, typeRef);
        return user.getId();
    }
}
