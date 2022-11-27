package com.example.electronicgradebook.configuration.security;

import com.example.electronicgradebook.resources.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.electronicgradebook.util.Values.ADMIN_AUTHORITY;
import static com.example.electronicgradebook.util.Values.USER_AUTHORITY;

public class MyUserPrincipal implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";
    private User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (user.getEmail().equals("admin")) {
            grantedAuthorities.add(new SimpleGrantedAuthority(ADMIN_AUTHORITY));
            grantedAuthorities.add(new SimpleGrantedAuthority(USER_AUTHORITY));
            return grantedAuthorities;
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(USER_AUTHORITY));
        return grantedAuthorities;
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