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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDto;
import pl.lodz.p.edu.grs.factory.UserFactory;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.UserUtil;


import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        verify(userRepository)
                .findAll(pageable);

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
        RegisterUserDto registerDTO = UserUtil.mockRegisterUserDTO();
        User user = UserUtil.mockUser();
        user.getRoles().clear();

        when(userFactory.createUser(registerDTO))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.registerUser(registerDTO);

        //then
        verify(userFactory)
                .createUser(registerDTO);
        verify(userRepository)
                .save(user);

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

    @Test
    public void shouldCreateAdminUser() throws Exception {
        //given
        RegisterUserDto registerDTO = UserUtil.mockRegisterUserDTO();
        User user = UserUtil.mockUser();

        when(userFactory.createUser(registerDTO))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.createSystemAdmin(registerDTO);

        //then
        verify(userFactory)
                .createUser(registerDTO);
        verify(userRepository)
                .save(user);

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
        assertThat(result.getRoles())
                .containsOnlyOnce(Role.SYSTEM_ADMIN);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        //given
        long userId = 1L;
        User user = UserUtil.mockUser();

        when(userRepository.getOne(userId))
                .thenReturn(user);

        //when
        userService.remove(userId);

        //then
        verify(userRepository)
                .getOne(userId);
        verify(userRepository)
                .delete(user);
    }

    @Test
    public void shouldUpdateUserPassword() throws Exception {
        //given
        User user = UserUtil.mockUser();

        String password = "new password";
        Long userId = UserUtil.USER_ID;

        when(userRepository.getOne(userId))
                .thenReturn(user);
        when(passwordEncoder.encode(password))
                .thenReturn(password);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.updatePassword(userId, password);

        //then
        verify(passwordEncoder)
                .encode(password);
        verify(userRepository)
                .save(user);

        assertThat(result.getPassword())
                .isNotBlank()
                .isEqualTo(password);
    }

    @Test
    public void shouldUpdateUserEmail() throws Exception {
        //given
        User user = UserUtil.mockUser();

        String email = "new@email.com";
        Long userId = UserUtil.USER_ID;

        when(userRepository.getOne(userId))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.updateEmail(userId, email);

        //then
        verify(userRepository)
                .save(user);

        assertThat(result.getEmail())
                .isNotBlank()
                .isEqualTo(email);
    }

    @Test
    public void shouldUpdateUserFirstNameAndLastName() {
        //given
        User user = UserUtil.mockUser();

        String firstName = "New First Name";
        String lastName = "New Last Name";

        Long userId = UserUtil.USER_ID;

        when(userRepository.getOne(userId))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);

        //when
        User result = userService.updateNames(userId, firstName, lastName);

        //then
        verify(userRepository)
                .save(user);

        assertThat(result.getFirstName())
                .isNotBlank()
                .isEqualTo(firstName);
        assertThat(result.getLastName())
                .isNotBlank()
                .isEqualTo(lastName);
    }

    @Test
    public void shouldReturnUserWhenFoundById() {
        //given
        User user = mock(User.class);

        when(userRepository.exists(UserUtil.USER_ID))
                .thenReturn(true);
        when(userRepository.findOne(UserUtil.USER_ID))
                .thenReturn(user);

        //when
        User result = userService.findOne(UserUtil.USER_ID);

        //then
        assertThat(result)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUserNotExist() {
        //given
        User user = mock(User.class);

        when(user.getId())
                .thenReturn(UserUtil.USER_ID);
        when(userRepository.exists(UserUtil.USER_ID))
                .thenReturn(false);

        //when
        userService.findOne(UserUtil.USER_ID);
    }

    @Test
    public void shouldBlockUser() throws Exception {
        //given
        User user = UserUtil.mockUser();
        when(userRepository.exists(user.getId()))
                .thenReturn(true);
        when(userRepository.findOne(user.getId()))
                .thenReturn(user);

        //then
        userService.blockUser(user.getId());

        //then
        verify(userRepository)
                .exists(user.getId());
        verify(userRepository)
                .findOne(user.getId());
        verify(userRepository)
                .save(user);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundExceptionWhenNotFoundByEmail() throws Exception {
        //given
        when(userRepository.findByEmail(UserUtil.EMAIL))
                .thenReturn(Optional.empty());

        //then
        userService.findByEmail(UserUtil.EMAIL);

        //then
    }
}
