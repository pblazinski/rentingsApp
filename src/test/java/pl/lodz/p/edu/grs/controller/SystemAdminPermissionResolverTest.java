package pl.lodz.p.edu.grs.controller;

import org.junit.Before;
import org.junit.Test;
import pl.lodz.p.edu.grs.model.user.Role;
import pl.lodz.p.edu.grs.model.user.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SystemAdminPermissionResolverTest {

    private SystemAdminPermissionResolver systemAdminPermissionResolver;

    @Before
    public void setUp() throws Exception {
        systemAdminPermissionResolver = new SystemAdminPermissionResolver();
    }

    @Test
    public void shouldReturnTrueWhenUserHasRoleSystemAdmin() {
        //given
        User user = mock(User.class);
        Set<Role> roles = new HashSet<>(Collections.singletonList(Role.SYSTEM_ADMIN));

        when(user.getRoles())
                .thenReturn(roles);
        //when
        boolean result = systemAdminPermissionResolver.isSystemAdmin(user);

        //then
        verify(user)
                .getRoles();

        assertThat(result)
                .isTrue();
    }

    @Test
    public void shouldReturnFalseWhenUserIsNotSystemAdmin() {
        //given
        User user = mock(User.class);
        Set<Role> roles = new HashSet<>(Collections.singletonList(Role.USER));

        when(user.getRoles())
                .thenReturn(roles);
        //when
        boolean result = systemAdminPermissionResolver.isSystemAdmin(user);

        //then
        verify(user)
                .getRoles();

        assertThat(result)
                .isFalse();
    }

}