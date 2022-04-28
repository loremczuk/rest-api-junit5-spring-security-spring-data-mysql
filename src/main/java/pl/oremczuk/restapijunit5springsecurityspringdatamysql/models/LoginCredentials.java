package pl.oremczuk.restapijunit5springsecurityspringdatamysql.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class LoginCredentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialsId;

    private String username;
    private String password;
    private String authority;

    @OneToMany (mappedBy = "loginCredentials")
    private List<Employee> employeeList;


    public LoginCredentials() {
    }

    public LoginCredentials(Long credentialsId, String username, String password, String authority) {
        this.credentialsId = credentialsId;
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public Long getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(Long credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
