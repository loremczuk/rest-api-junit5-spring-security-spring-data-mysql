package pl.oremczuk.restapijunit5springsecurityspringdatamysql;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.EmployeeRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;

import javax.persistence.EntityNotFoundException;
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
    public Object[] getEmployeeDataWithLimitedInfo() {

        return employeeRepository.findEmployeeDataWithLimitedInfo();

    }

    @GetMapping("/full")
    public Object[] getAllEmployees() {

        return employeeRepository.findFullEmployeeDetails();

    }

    @GetMapping("credentials")
    public List<LoginCredentials> getAllLoginCredentials() {

        return loginCredentialsRepository.findAll();

    }

    @PatchMapping("/{employeeId}/{position}")
    public ResponseEntity<Employee> updateGivenEmployeePosition(@PathVariable Long employeeId,
                                                      @PathVariable String position) {


        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        employee.setPosition(position);

        Employee updatedEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(employee);

    }

    @PostMapping
    public ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee) {

        Employee newEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(employee);

    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<HttpStatus> deleteGivenEmployee(@PathVariable Long employeeId) {

        Employee employeeToBeDeleted = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        employeeRepository.delete(employeeToBeDeleted);

        return ResponseEntity.ok(HttpStatus.OK);

    }



}
