package br.dev.optimus.ged.cpanel.request;

import br.dev.optimus.ged.cpanel.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank String name,
        @Pattern(regexp = "^[0-9]{2}(9?)[0-9]{8}$") String phone,
        @Pattern(regexp = "^[0-9]{11}([0-9]{3})?$") String identity,
        @NotBlank String role,
        @Email String email,
        @Pattern(regexp = "^[A-Za-z0-9_.-]{3,30}") String username,
        String password,
        boolean active) {

    public User toUser() {
        return User.builder()
                .name(name.toUpperCase())
                .phone(phone)
                .identity(identity)
                .role(role.toUpperCase())
                .email(email.toLowerCase())
                .username(username.toLowerCase())
                .password(password)
                .build();
    }
}
