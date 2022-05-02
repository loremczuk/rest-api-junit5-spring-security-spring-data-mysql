package pl.oremczuk.restapijunit5springsecurityspringdatamysql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.Employee;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.models.LoginCredentials;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.EmployeeRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.repositories.LoginCredentialsRepository;
import pl.oremczuk.restapijunit5springsecurityspringdatamysql.security.MyUserDetailsService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WebMvcTest
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private LoginCredentialsRepository loginCredentialsRepository;

    @Autowired
    private WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator;

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
    public void shouldDenyAccessToLimitedInfoForUnauthenticatedUsers() throws Exception {

        Mockito.when(employeeRepository.findEmployeeDataWithLimitedInfo()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void shouldDenyAccessToFullEmployeeDataForUnauthorizedUsers() throws Exception {

        Mockito.when(employeeRepository.findFullEmployeeDetails()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/full")
                    .with(SecurityMockMvcRequestPostProcessors.user("user").password("password1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void shouldAllowAccessToLoginCredentialsForAdmin() throws Exception {

        List<LoginCredentials> credentialsList = Collections.singletonList(loginCredentials);

        Mockito.when(loginCredentialsRepository.findAll()).thenReturn(credentialsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/credentials")
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").password("password2").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldUpdateEmployeeData() throws Exception {

        Optional<Employee> givenEmployee = Optional.of(employee);

        Mockito.when(employeeRepository.findById(12L)).thenReturn(givenEmployee);

        mockMvc.perform(MockMvcRequestBuilders.patch("/employees/" + givenEmployee.get().getEmployeeId() + "/" + givenEmployee.get().getPosition())
                    .content("software tester")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "password2"))
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void shouldEvaluateAccessPermissions() {

        Authentication user = new TestingAuthenticationToken("user", "password1", "ROLE_USER");
        Authentication admin = new TestingAuthenticationToken("admin", "password2", "ROLE_ADMIN");

        Assertions.assertAll(
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees", "GET", user)),
        () -> assertFalse(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/full", "GET", user)),
        () -> assertFalse(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/credentials", "GET", user)),
        () -> assertFalse(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/{employeeId}/{position}", "PATCH", user)),
        () -> assertFalse(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees", "POST", user)),
        () -> assertFalse(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/{employeeId}", "DELETE", user)),


        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees", "GET", admin)),
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/full", "GET", admin)),
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/credentials", "GET", admin)),
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/{employeeId}/{position}", "PATCH", admin)),
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees", "POST", admin)),
        () -> assertTrue(webInvocationPrivilegeEvaluator.isAllowed(null, "/employees/{employeeId}", "DELETE", admin))
        );

    }

}
