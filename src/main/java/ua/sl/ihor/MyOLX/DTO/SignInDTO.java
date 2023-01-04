package ua.sl.ihor.MyOLX.DTO;

import lombok.Data;

@Data
public class SignInDTO {
    private String email;
    private String password;
    private boolean rememberMe;
}
