package pl.lodz.p.edu.grs.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final Role USER = Role.USER;
    private static final Role SYSTEM_ADMIN = Role.SYSTEM_ADMIN;

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    private static final String EMAIL = "EMAIL@EMAIL";
    private static final String PASSWORD = "PASSWORD";

    @Before
    public void setUp() throws Exception {
        this.userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void shouldReturnAppUserWhenFoundUser() throws Exception {
        //given
        User user = mock(User.class);

        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        Set<Role> roles = new HashSet<>(Arrays.asList(USER, SYSTEM_ADMIN));
        when(user.getRoles())
                .thenReturn(roles);
        when(user.getEmail())
                .thenReturn(EMAIL);
        when(user.getPassword())
                .thenReturn(PASSWORD);

        //when
        UserDetails result = userDetailsService.loadUserByUsername(EMAIL);

        //then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(AppUser.class);
        assertThat(result.getUsername())
                .isNotBlank()
                .isEqualTo(EMAIL);
        assertThat(result.getPassword())
                .isNotBlank()
                .isEqualTo(PASSWORD);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundExceptionWhenUserNotExisting() throws Exception {
        //given
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        //when
        userDetailsService.loadUserByUsername(EMAIL);

        //then
        verify(userRepository)
                .findByEmail(EMAIL);
    }
}