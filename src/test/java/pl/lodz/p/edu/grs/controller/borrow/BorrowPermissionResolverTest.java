package pl.lodz.p.edu.grs.controller.borrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.lodz.p.edu.grs.controller.SystemAdminPermissionResolver;
import pl.lodz.p.edu.grs.model.Borrow;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.repository.BorrowRepository;
import pl.lodz.p.edu.grs.repository.UserRepository;
import pl.lodz.p.edu.grs.security.AppUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static pl.lodz.p.edu.grs.util.UserUtil.EMAIL;
import static pl.lodz.p.edu.grs.util.UserUtil.USER_ID;

@RunWith(MockitoJUnitRunner.class)
public class BorrowPermissionResolverTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SystemAdminPermissionResolver systemAdminPermissionResolver;
    @Mock
    private BorrowRepository borrowRepository;

    private BorrowPermissionResolver borrowPermisionResolver;

    @Before
    public void setUp() throws Exception {
        borrowPermisionResolver = new BorrowPermissionResolver(userRepository, systemAdminPermissionResolver, borrowRepository);
    }

    @Test
    public void shouldCompleteSuccessfully() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(borrowRepository.exists(borrowId))
                .thenReturn(true);
        when(borrowRepository.findOne(borrowId))
                .thenReturn(borrow);
        when(systemAdminPermissionResolver.isSystemAdmin(user))
                .thenReturn(false);
        when(appUser.hasAuthority(Authority.GET_BORROW))
                .thenReturn(true);
        when(user.getId())
                .thenReturn(USER_ID);
        when(borrow.getUser())
                .thenReturn(user);

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(borrowRepository)
                .exists(borrowId);
        verify(borrowRepository)
                .findOne(borrowId);
        verify(systemAdminPermissionResolver)
                .isSystemAdmin(user);
        verify(appUser)
                .hasAuthority(Authority.GET_BORROW);

        assertThat(result)
                .isTrue();
    }

    @Test
    public void shouldCompleteSuccessfullyWhenIsSystemAdmin() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(borrowRepository.exists(borrowId))
                .thenReturn(true);
        when(borrowRepository.findOne(borrowId))
                .thenReturn(borrow);
        when(systemAdminPermissionResolver.isSystemAdmin(user))
                .thenReturn(true);

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(borrowRepository)
                .exists(borrowId);
        verify(borrowRepository)
                .findOne(borrowId);
        verify(systemAdminPermissionResolver)
                .isSystemAdmin(user);

        verifyNoMoreInteractions(appUser);
        verifyNoMoreInteractions(user);
        verifyNoMoreInteractions(borrow);

        assertThat(result)
                .isTrue();
    }

    @Test
    public void shouldReturnFalseWhenNotSameUser() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        User secondUser = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(borrowRepository.exists(borrowId))
                .thenReturn(true);
        when(borrowRepository.findOne(borrowId))
                .thenReturn(borrow);
        when(systemAdminPermissionResolver.isSystemAdmin(user))
                .thenReturn(false);
        when(appUser.hasAuthority(Authority.GET_BORROW))
                .thenReturn(true);
        when(user.getId())
                .thenReturn(USER_ID);
        when(borrow.getUser())
                .thenReturn(secondUser);
        when(secondUser.getId())
                .thenReturn(USER_ID + 1L);

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(borrowRepository)
                .exists(borrowId);
        verify(borrowRepository)
                .findOne(borrowId);
        verify(systemAdminPermissionResolver)
                .isSystemAdmin(user);

        assertThat(result)
                .isFalse();
    }

    @Test
    public void shouldReturnFalseWhenNotHasGetBorrowAuthority() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(borrowRepository.exists(borrowId))
                .thenReturn(true);
        when(borrowRepository.findOne(borrowId))
                .thenReturn(borrow);
        when(systemAdminPermissionResolver.isSystemAdmin(user))
                .thenReturn(false);
        when(appUser.hasAuthority(Authority.GET_BORROW))
                .thenReturn(false);

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(borrowRepository)
                .exists(borrowId);
        verify(borrowRepository)
                .findOne(borrowId);
        verify(systemAdminPermissionResolver)
                .isSystemAdmin(user);

        verifyNoMoreInteractions(user);
        verifyNoMoreInteractions(borrow);

        assertThat(result)
                .isFalse();
    }

    @Test
    public void shouldReturnFalseWhenNotFoundBorrow() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        when(borrowRepository.exists(borrowId))
                .thenReturn(false);

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);
        verify(borrowRepository)
                .exists(borrowId);

        verifyNoMoreInteractions(borrowRepository);
        verifyNoMoreInteractions(systemAdminPermissionResolver);
        verifyNoMoreInteractions(user);
        verifyNoMoreInteractions(borrow);

        assertThat(result)
                .isFalse();
    }

    @Test
    public void shouldReturnFalseWhenNotFoundUser() throws Exception {
        //given
        AppUser appUser = mock(AppUser.class);
        User user = mock(User.class);
        Borrow borrow = mock(Borrow.class);
        final long borrowId = 1L;

        when(appUser.getUsername())
                .thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        //when
        boolean result = borrowPermisionResolver.hasAuthoritiyToGetBorrow(appUser, borrowId);

        //then
        verify(appUser)
                .getUsername();
        verify(userRepository)
                .findByEmail(EMAIL);

        verifyZeroInteractions(borrowRepository);
        verifyZeroInteractions(systemAdminPermissionResolver);
        verifyZeroInteractions(user);
        verifyZeroInteractions(borrow);

        assertThat(result)
                .isFalse();
    }
}