package pl.lodz.p.edu.grs.controller.user;

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
import pl.lodz.p.edu.grs.security.AppUser;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.StubHelper;
import pl.lodz.p.edu.grs.util.UserUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserDELETERemoveUserEndpointTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private User user;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
        user = StubHelper.stubUser();
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = delete(String.format("/api/users/%d/", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isOk());

        boolean user = userRepository.exists(this.user.getId());
        assertThat(user)
                .isFalse();
    }

    @Test
    public void shouldReturnUnauthorizedWhenNotAuthorized() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = delete(String.format("/api/users/%d/", user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnForbiddenWhenNotOwner() throws Exception {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setEmail("other@email");
        User user = userService.registerUser(userDTO);

        MockHttpServletRequestBuilder requestBuilder = delete(String.format("/api/users/%d/", this.user.getId()))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(user(new AppUser(user)))
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        //when
        ResultActions result = mockMvc.perform(requestBuilder);

        //then
        result.andExpect(status().isForbidden());
    }

}
