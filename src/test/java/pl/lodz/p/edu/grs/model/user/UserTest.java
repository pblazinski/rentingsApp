package pl.lodz.p.edu.grs.model.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    private User user;

    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String EMAIL = "email@correct.email";
    private static final String PASSWORD = "password";
    private static final Role ROLE = Role.USER;

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @Test
    public void shouldSetNames() throws Exception {
        //given

        //when
        user.updateNames(FIRST_NAME, LAST_NAME);

        //then
        assertThat(user.getFirstName())
                .isNotBlank()
                .isEqualTo(FIRST_NAME);
        assertThat(user.getLastName())
                .isNotBlank()
                .isEqualTo(LAST_NAME);
    }

    @Test
    public void shouldReplaceUserNames() throws Exception {
        //given
        String oldName = "name";
        user.updateNames(oldName, oldName);

        //when
        user.updateNames(FIRST_NAME, LAST_NAME);

        //then
        assertThat(user.getFirstName())
                .isNotBlank()
                .isNotSameAs(oldName)
                .isEqualTo(FIRST_NAME);
        assertThat(user.getLastName())
                .isNotBlank()
                .isNotSameAs(oldName)
                .isEqualTo(LAST_NAME);
    }

    @Test
    public void shouldSetEmail() throws Exception {
        //given

        //when
        user.updateEmail(EMAIL);

        //then
        assertThat(user.getEmail())
                .isNotBlank()
                .isEqualTo(EMAIL);
    }

    @Test
    public void shouldReplaceEmail() throws Exception {
        //given
        String oldEmail = "email@email";
        user.updateEmail(oldEmail);

        //when
        user.updateEmail(EMAIL);

        //then
        assertThat(user.getEmail())
                .isNotBlank()
                .isNotSameAs(oldEmail)
                .isEqualTo(EMAIL);
    }

    @Test
    public void shouldSetPassword() throws Exception {
        //given

        //when
        user.updatePassword(PASSWORD);

        //then
        assertThat(user.getPassword())
                .isNotBlank()
                .isEqualTo(PASSWORD);
    }

    @Test
    public void shouldReplacePassword() throws Exception {
        //given
        String oldPassword = "old password";
        user.updatePassword(oldPassword);

        //when
        user.updatePassword(PASSWORD);

        //then
        assertThat(user.getPassword())
                .isNotBlank()
                .isNotSameAs(oldPassword)
                .isEqualTo(PASSWORD);
    }

    @Test
    public void shouldSetRole() throws Exception {
        //given

        //when
        user.grant(ROLE);

        //then
        assertThat(user.getRoles())
                .isNotEmpty()
                .containsOnly(ROLE);
    }

    @Test
    public void shouldRemoveRole() throws Exception {
        //given
        user.grant(ROLE);

        //when
        user.revoke(ROLE);

        //then
        assertThat(user.getRoles())
                .doesNotContain(ROLE)
                .isEmpty();
    }
}