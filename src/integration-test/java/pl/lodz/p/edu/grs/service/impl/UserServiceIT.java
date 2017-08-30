package pl.lodz.p.edu.grs.service.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.CategoryRepository;
import pl.lodz.p.edu.grs.repository.GameRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.BorrowService;
import pl.lodz.p.edu.grs.service.CategoryService;
import pl.lodz.p.edu.grs.service.GameService;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.UserUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceIT {

    private static final String BLANK_VALUE = "     ";

    @Autowired
    private BorrowRepository borrowRepository;


    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;


    @After
    public void tearDown() throws Exception {
        borrowRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnPageWithOneUser() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);

        //when
        Page<User> users = userService.findAll(new PageRequest(0, 10));

        assertThat(users.getTotalElements())
                .isEqualTo(1);
        User resultUser = users.getContent().get(0);

        assertThat(resultUser.getFirstName())
                .isEqualTo(user.getFirstName());
        assertThat(resultUser.getLastName())
                .isEqualTo(user.getLastName());
        assertThat(resultUser.getEmail())
                .isEqualTo(user.getEmail());
        assertThat(resultUser.getPassword())
                .isEqualTo(user.getPassword());
    }

    @Test
    public void shouldRegisterNewUser() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();

        //when
        User result = userService.registerUser(userDTO);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getEmail())
                .isNotEmpty()
                .isEqualTo(UserUtil.EMAIL);
        assertThat(result.getFirstName())
                .isNotEmpty()
                .isEqualTo(UserUtil.FIRST_NAME);
        assertThat(result.getLastName())
                .isNotEmpty()
                .isEqualTo(UserUtil.LAST_NAME);
        assertThat(result.getRoles())
                .isNotEmpty()
                .containsOnlyOnce(Role.USER);
    }

    @Test
    public void shouldCreateSystemAdmin() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();

        //when
        User result = userService.createSystemAdmin(userDTO);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getEmail())
                .isNotEmpty()
                .isEqualTo(UserUtil.EMAIL);
        assertThat(result.getFirstName())
                .isNotEmpty()
                .isEqualTo(UserUtil.FIRST_NAME);
        assertThat(result.getLastName())
                .isNotEmpty()
                .isEqualTo(UserUtil.LAST_NAME);
        assertThat(result.getRoles())
                .isNotEmpty()
                .containsOnlyOnce(Role.SYSTEM_ADMIN);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenRegisterUserWithBlankFirstName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setFirstName(BLANK_VALUE);

        //when
        userService.registerUser(userDTO);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenRegisterUserWithBlankLastName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setLastName(BLANK_VALUE);

        //when
        userService.registerUser(userDTO);

        //then
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowConstraintViolationExceptionWhenRegisterUserWithNotValidEmail() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        userDTO.setEmail("aaaa@");

        //when
        userService.registerUser(userDTO);
        //then
    }

    @Test
    public void shouldRemoveUser() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();

        //when
        userService.remove(userId);

        //then
        boolean exists = userRepository.exists(userId);
        assertThat(exists)
                .isFalse();
    }

    @Test
    public void shouldUpdateEmail() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();
        String email = "new@email.com";

        //when
        User result = userService.updateEmail(userId, email);

        //then
        assertThat(result.getEmail())
                .isNotBlank()
                .isEqualTo(email);
    }

    @Test
    public void shouldUpdateNames() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();
        String firstName = "First";
        String lastName = "Last";

        //when
        User result = userService.updateNames(userId, firstName, lastName);

        //then
        assertThat(result.getFirstName())
                .isNotBlank()
                .isEqualTo(firstName);
        assertThat(result.getLastName())
                .isNotBlank()
                .isEqualTo(lastName);
    }

    @Test
    public void shouldUpdatePassword() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();
        String password = user.getPassword();
        String newPassword = "First";

        //when
        User result = userService.updatePassword(userId, newPassword);

        //then
        assertThat(result.getPassword())
                .isNotSameAs(password);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdateEmailWithNotValidEmail() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();
        String email = "new@";

        //when
        Throwable throwable = catchThrowable(() -> userService.updateEmail(userId, email));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdateNamesWithBlankFirstName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();

        //when
        Throwable throwable = catchThrowable(() -> userService.updateNames(userId, BLANK_VALUE, user.getLastName()));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenUpdateNamesWithBlankLastName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();

        //when
        Throwable throwable = catchThrowable(() -> userService.updateNames(userId, user.getFirstName(), BLANK_VALUE));

        //then
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateEmailForNotExistedUser() {
        //given

        //when
        userService.updateEmail(1L, UserUtil.EMAIL);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdateNamesForNotExistedUser() {
        //given

        //when
        userService.updateNames(1L, UserUtil.FIRST_NAME, UserUtil.LAST_NAME);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenUpdatePasswordForNotExistedUser() {
        //given

        //when
        userService.updatePassword(1L, UserUtil.PASSWORD);

        //then
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenNotFoundEntity() {
        //given

        //when
        userService.findOne(1L);

        //then
    }

    @Test
    public void shouldReturnUserWhenExists() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);

        //when
        User result = userService.findOne(user.getId());

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getId())
                .isNotNull()
                .isEqualTo(user.getId());
        assertThat(result.getEmail())
                .isNotEmpty()
                .isEqualTo(user.getEmail());
    }

}

