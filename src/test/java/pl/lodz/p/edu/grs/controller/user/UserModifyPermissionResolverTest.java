package pl.lodz.p.edu.grs.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserModifyPermissionResolverTest {

    @Mock
    private UserRepository userRepository;

    private UserModifyPermissionResolver userModifyPermissionResolver;

    private static final long USER_ID = 1L;
    private static final String EMAIL = "email@email";

    @Before
    public void setUp() throws Exception {
        this.userModifyPermissionResolver = new UserModifyPermissionResolver(userRepository);
    }

    @Test
    public void shouldCompleteSuccessfully() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(appUser.hasAuthority(Authority.MODIFY_USER))
                .thenReturn(true);
        when(user.getId())
                .thenReturn(USER_ID);

        //when
        boolean result = userModifyPermissionResolver.hasAuthorityToModifyUser(appUser, USER_ID);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(appUser)
                .hasAuthority(Authority.MODIFY_USER);
        verify(user)
                .getId();

        assertThat(result)
                .isTrue();
    }

    @Test
    public void shouldReturnFalseWhenUserNotExisting() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        //when
        boolean result = userModifyPermissionResolver.hasAuthorityToModifyUser(appUser, USER_ID);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);

        assertThat(result)
                .isFalse();
    }

    @Test
    public void shouldReturnFalseWhenNotHaveAuthorityToModifyUser() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(appUser.hasAuthority(Authority.MODIFY_USER))
                .thenReturn(false);

        //when
        boolean result = userModifyPermissionResolver.hasAuthorityToModifyUser(appUser, USER_ID);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(appUser)
                .hasAuthority(Authority.MODIFY_USER);

        assertThat(result)
                .isFalse();
    }

    @Test
    public void shouldReturnFalseWhenUserIdIsDifferent() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(appUser.hasAuthority(Authority.MODIFY_USER))
                .thenReturn(true);
        when(user.getId())
                .thenReturn(USER_ID + 1);

        //when
        boolean result = userModifyPermissionResolver.hasAuthorityToModifyUser(appUser, USER_ID);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(appUser)
                .hasAuthority(Authority.MODIFY_USER);
        verify(user)
                .getId();

        assertThat(result)
                .isFalse();
    }
}