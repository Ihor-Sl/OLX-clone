package ua.sl.ihor.MyOLX.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_MODER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
