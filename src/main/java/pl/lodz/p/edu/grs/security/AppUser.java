package pl.lodz.p.edu.grs.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.edu.grs.model.user.Authority;
import pl.lodz.p.edu.grs.model.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class AppUser
        implements UserDetails {

    private User user;

    private Collection<GrantedAuthority> authorities;

    public AppUser(final User user) {
        this.user = user;
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = this.user.getRoles()
                .stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toSet());
        this.authorities = Collections.unmodifiableSet(simpleGrantedAuthorities);
    }

    public boolean hasAuthority(final Authority authority) {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.name());
        return this.authorities.contains(grantedAuthority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
