package pl.lodz.p.edu.grs.service.impl;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import pl.lodz.p.edu.grs.Application;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.service.UserService;
import pl.lodz.p.edu.grs.util.UserUtil;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceIT {

    private static final String BLANK_VALUE = "     ";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() throws Exception {
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
        User result = userService.updatePassword(userId, password);

        //then
        assertThat(result.getPassword())
                .isNotSameAs(password);
    }

    @Test(expected = TransactionSystemException.class)
    public void shouldThrowTransactionSystemExceptionWhenUpdateEmailWithNotValidEmail() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();
        String email = "new@";

        //when
        userService.updateEmail(userId, email);

        //then
    }

    @Test(expected = TransactionSystemException.class)
    public void shouldThrowTransactionSystemExceptionWhenUpdateNamesWithBlankFirstName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();

        //when
        userService.updateNames(userId, BLANK_VALUE, user.getLastName());

        //then
    }

    @Test(expected = TransactionSystemException.class)
    public void shouldThrowTransactionSystemExceptionWhenUpdateNamesWithBlankLastName() {
        //given
        RegisterUserDTO userDTO = UserUtil.mockRegisterUserDTO();
        User user = userService.registerUser(userDTO);
        Long userId = user.getId();

        //when
        userService.updateNames(userId, user.getFirstName(), BLANK_VALUE);

        //then
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

}

