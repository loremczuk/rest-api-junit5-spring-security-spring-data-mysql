package pl.oremczuk.restapijunit5springsecurityspringdatamysql.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private final LoginCredentials loginCredentials;

    public MyUserDetails(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(loginCredentials.getAuthority());
        return Arrays.asList(authority);

    }

    @Override
    public String getPassword() {
        return loginCredentials.getPassword();
    }

    @Override
    public String getUsername() {
        return loginCredentials.getUsername();
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
