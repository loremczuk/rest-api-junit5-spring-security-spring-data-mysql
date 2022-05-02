package pl.oremczuk.restapijunit5springsecurityspringdatamysql;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.EmployeeRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DaoTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoginCredentialsRepository loginCredentialsRepository;

    @BeforeEach
    public void setAndSaveAnEmployee() {

        Employee employee = new Employee(12L,"Jan", "Motyka", "junior devop", "4000.00", "ROLE_USER");

        employeeRepository.save(employee);
    }

    @Test
    public void shouldGetEmployeesWithLimitedInfo() {

        employeeRepository.findEmployeeDataWithLimitedInfo();

        assertArrayEquals(new Object[]{"Jan", "Motyka", "junior devop"}, new Object[]{"Jan", "Motyka", "junior devop"});

    }

    @Test
    public void shouldGetFullEmployeeData() {

        employeeRepository.findFullEmployeeDetails();

        assertArrayEquals(new Object[]{"Jan", "Motyka", "junior devop", "4000.00", "ROLE_USER"}, new Object[]{"Jan", "Motyka", "junior devop", "4000.00", "ROLE_USER"});

    }

    @Test
    public void shouldGetCredentials() {

        LoginCredentials loginCredentials = new LoginCredentials(3L, "user", "password", "ROLE_USER");

        loginCredentialsRepository.save(loginCredentials);

        List<LoginCredentials> actualList = loginCredentialsRepository.findAll();

        List<LoginCredentials> expectedList = new ArrayList<>();
        expectedList.add(loginCredentials);

        assertEquals(expectedList.toString(), expectedList.toString());

    }

}
