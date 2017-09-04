package pl.lodz.p.edu.grs.model.user;

import sun.security.x509.AuthorityInfoAccessExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Role {

    SYSTEM_ADMIN(Authority.LIST_USER, Authority.MODIFY_USER, Authority.MODIFY_GAME, Authority.MODIFY_CATEGORY, Authority.LIST_BORROW, Authority.MODIFY_BORROW),

    USER(Authority.MODIFY_USER, Authority.GET_USER, Authority.LIST_GAME, Authority.GET_GAME, Authority.GET_CATEGORY, Authority.LIST_CATEGORY, Authority.LIST_BORROW, Authority.GET_BORROW);

    Role(final Authority... authorities) {
        this.authorities = Arrays.asList(authorities);
    }

    private final List<Authority> authorities;

    public List<Authority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }
}