package pl.oremczuk.restapijunit5springsecurityspringdatamysql;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.EmployeeRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.security.MyUserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@WebMvcTest(EmployeeDataController.class)
@WithMockUser(username = "user", password = "password1", roles = "ADMIN")
public class RestControllerTests {

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private LoginCredentialsRepository loginCredentialsRepository;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    private Employee employee;
    private Object[] employees;
    private LoginCredentials loginCredentials;

    @BeforeEach
    public void setEmployeeData() {

        employee = new Employee(12L,"Jan", "Motyka", "junior devops", "4000.00", "ROLE_USER");
        loginCredentials = new LoginCredentials(3L,"typicalUser","secretPassword","ROLE_USER");

        employees = new Object[] {employee, loginCredentials};
    }

    @Test
    public void shouldFindAllEmployeesWithLimitedInfo() throws Exception {


        Mockito.when(employeeRepository.findEmployeeDataWithLimitedInfo()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("Jan")));

    }

    @Test
    public void shouldFindAllEmployees() throws Exception {

        Mockito.when(employeeRepository.findFullEmployeeDetails()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/full"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username", Matchers.is("typicalUser")));

    }

    @Test
    public void shouldGetCredentials() throws Exception {

        List<LoginCredentials> credentialsList = Collections.singletonList(loginCredentials);

        Mockito.when(loginCredentialsRepository.findAll()).thenReturn(credentialsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/credentials"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authority", Matchers.is("ROLE_USER")));

    }

    @Test
    public void shouldUpdateEmployeeData() throws Exception {

        Optional<Employee> givenEmployee = Optional.of(employee);

        Mockito.when(employeeRepository.findById(12L)).thenReturn(givenEmployee);

        mockMvc.perform(MockMvcRequestBuilders.patch("/employees/" + givenEmployee.get().getEmployeeId() + "/" + givenEmployee.get().getPosition()).content("software tester"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void shouldAddNewEmployee() throws Exception {

        String employeeInJson =   """
                    {
                        "firstName":"Jan",
                        "lastName":"Motyka",
                        "position":"junior devops",
                        "salary":"40000.00",
                        "authority":"ROLE_USER"
                    }
                    """;

        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees/")
                    .content(employeeInJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void shouldDeleteEmployee() throws Exception {

        Mockito.when(employeeRepository.findById(12L)).thenReturn(Optional.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/" + employee.getEmployeeId())
                    .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
