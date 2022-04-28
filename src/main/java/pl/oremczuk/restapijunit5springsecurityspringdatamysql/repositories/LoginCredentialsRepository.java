package pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;

public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials, Long> {
}
