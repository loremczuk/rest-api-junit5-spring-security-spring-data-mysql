package pl.oremczuk.restapijunit5springsecurityspringdatamysql;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.EmployeeRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeDataController {

    private final EmployeeRepository employeeRepository;
    private final LoginCredentialsRepository loginCredentialsRepository;

    public EmployeeDataController(EmployeeRepository employeeRepository, LoginCredentialsRepository loginCredentialsRepository) {
        this.employeeRepository = employeeRepository;
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    @GetMapping
    public Object[] getEmployeeDataWithoutSalary() {

        return employeeRepository.findEmployeeDataWithLimitedInfo();

    }

    @GetMapping("/full")
    public Object[] getAllEmployees() {

        return employeeRepository.findFullEmployeeDetails();

    }
}
