package pl.lodz.p.edu.grs.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.edu.grs.controller.user.RegisterUserDTO;
import pl.lodz.p.edu.grs.model.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserFactoryTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserFactory userFactory;

    private static final String EMAIL = "correct@email.com";

    private static final String FIRST_NAME = "first";

    private static final String LAST_NAME = "last";

    private static final String PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        userFactory = new UserFactory(passwordEncoder);
    }

    @Test
    public void shouldCreateUserObject() throws Exception {
        //given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);

        when(passwordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);
        //when
        User result = userFactory.createUser(registerUserDTO);

        //then
        verify(passwordEncoder)
                .encode(PASSWORD);
        assertThat(result)
                .isNotNull();
        assertThat(result.getEmail())
                .isNotBlank()
                .isEqualTo(EMAIL);
        assertThat(result.getFirstName())
                .isNotBlank()
                .isEqualTo(FIRST_NAME);
        assertThat(result.getLastName())
                .isNotBlank()
                .isEqualTo(LAST_NAME);
        assertThat(result.getPassword())
                .isNotBlank()
                .isEqualTo(PASSWORD);
    }

    @Test
    public void shouldCreateTwoDifferentObject() throws Exception {
        //given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD);

        when(passwordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);
        User user = userFactory.createUser(registerUserDTO);

        //when
        User result = userFactory.createUser(registerUserDTO);

        //then
        assertThat(result)
                .isNotSameAs(user);
    }

}