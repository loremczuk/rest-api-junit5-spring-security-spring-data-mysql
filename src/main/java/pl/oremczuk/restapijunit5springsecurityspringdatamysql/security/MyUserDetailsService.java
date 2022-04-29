package pl.oremczuk.restapijunit5springsecurityspringdatamysql.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final LoginCredentialsRepository loginCredentialsRepository;

    public MyUserDetailsService(LoginCredentialsRepository loginCredentialsRepository) {
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginCredentials credentials = loginCredentialsRepository.getCredentialsByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new MyUserDetails(credentials);

    }
}
