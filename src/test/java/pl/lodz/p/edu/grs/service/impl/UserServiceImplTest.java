package pl.lodz.p.edu.grs.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.factory.UserFactory;
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.UserUtil;


import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserFactory userFactory;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.userService = new UserServiceImpl(userRepository, userFactory, passwordEncoder);
    }

    @Test
    public void shouldReturnPageOfUsers() throws Exception {
        //given
        Pageable pageable = new PageRequest(0, 10);
        User user = new User();
        List<User> users = Collections.singletonList(user);
        PageImpl<User> usersPage = new PageImpl<>(users);

        when(userRepository.findAll(pageable))
                .thenReturn(usersPage);

        //when
        Page<User> result = userService.findAll(pageable);

        //then
        assertThat(result)
                .isEqualTo(usersPage);
        assertThat(result.getContent())
                .containsExactly(user);
        assertThat(result.getTotalElements())
                .isEqualTo(users.size());
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        //given
        RegisterUserDTO registerDTO = UserUtil.mockRegisterUserDTO();
        User user = UserUtil.mockUser();

        when(userFactory.createUser(registerDTO))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.registerUser(registerDTO);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getFirstName())
                .isNotBlank()
                .isEqualTo(UserUtil.FIRST_NAME);
        assertThat(result.getLastName())
                .isNotBlank()
                .isEqualTo(UserUtil.LAST_NAME);
        assertThat(result.getEmail())
                .isNotBlank()
                .isEqualTo(UserUtil.EMAIL);
        assertThat(result.getPassword())
                .isNotBlank()
                .isEqualTo(UserUtil.PASSWORD);
    }

    //TODO add test for remove update Password Email and Names
}