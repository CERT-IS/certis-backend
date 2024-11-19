package certis.CertisHomepage.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public enum RoleType {
    ROLE_ADMIN, ROLE_USER;

    public List<GrantedAuthority> toGrantedAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.name()));
    }
}
