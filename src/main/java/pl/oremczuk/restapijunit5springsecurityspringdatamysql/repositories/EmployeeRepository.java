package pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT emp.firstName, emp.lastName, emp.position FROM Employee emp")
    Object[] findEmployeeDataWithLimitedInfo();

    @Query("SELECT emp.firstName, emp.lastName, emp.position, emp.salary, emp.authority, cred.username, cred.password FROM Employee emp, LoginCredentials cred WHERE emp.authority = cred.authority")
    Object[] findFullEmployeeDetails();

}
