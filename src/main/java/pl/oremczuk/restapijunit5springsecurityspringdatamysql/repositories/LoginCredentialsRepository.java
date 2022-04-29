package pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;

import java.util.Optional;

public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials, Long> {

    @Query("SELECT cred from LoginCredentials cred WHERE cred.username = :username")
    Optional<LoginCredentials> getCredentialsByUsername(String username);
}
