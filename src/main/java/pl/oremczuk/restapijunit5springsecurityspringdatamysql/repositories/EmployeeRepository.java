package pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
