package engine.dto;

import engine.validation.CustomEmail;
import javax.validation.constraints.Size;

public class UserRegistrationDTO {
    @CustomEmail
    private String email;
    @Size(min = 5)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
