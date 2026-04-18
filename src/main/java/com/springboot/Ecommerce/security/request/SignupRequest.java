package com.springboot.Ecommerce.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Email
    @Size(max = 30)
    private String email;
    private Set<String> role;
    @NotBlank
    @Size(min = 6, max = 35)
    private String password;
}
