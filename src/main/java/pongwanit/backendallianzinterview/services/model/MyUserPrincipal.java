package pongwanit.backendallianzinterview.services.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pongwanit.backendallianzinterview.repository.model.UserAuth;

import java.util.Collection;

public class MyUserPrincipal implements UserDetails {
    private UserAuth userAuth;

    public MyUserPrincipal(UserAuth user) {
        this.userAuth = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.userAuth.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userAuth.getUsername();
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
