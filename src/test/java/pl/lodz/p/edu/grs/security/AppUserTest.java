package pl.lodz.p.edu.grs.security;


import org.junit.Test;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.util.UserUtil;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppUserTest {

    @Test
    public void shouldCreateAppUser() {
        //given
        User user = mock(User.class);
        HashSet<Role> roles = new HashSet<>(Collections.singletonList(Role.USER));

        when(user.getPassword())
                .thenReturn(UserUtil.PASSWORD);
        when(user.getEmail())
                .thenReturn(UserUtil.EMAIL);
        when(user.getRoles())
                .thenReturn(roles);

        //when
        AppUser result = new AppUser(user);

        //then
        assertThat(result.getPassword())
                .isSameAs(UserUtil.PASSWORD);
        assertThat(result.getUsername())
                .isSameAs(UserUtil.EMAIL);
        assertThat(result.isAccountNonExpired())
                .isTrue();
        assertThat(result.isEnabled())
                .isTrue();
        assertThat(result.isAccountNonLocked())
                .isTrue();
        assertThat(result.isCredentialsNonExpired())
                .isTrue();
    }

}