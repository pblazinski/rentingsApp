package pl.lodz.p.edu.grs.model.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Role {

    SYSTEM_ADMIN(Authority.LIST_USER, Authority.MODIFY_USER, Authority.LIST_BORROWS),

    USER(Authority.MODIFY_USER, Authority.GET_USER);

    Role(final Authority... authorities) {
        this.authorities = Arrays.asList(authorities);
    }

    private final List<Authority> authorities;

    public List<Authority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }
}