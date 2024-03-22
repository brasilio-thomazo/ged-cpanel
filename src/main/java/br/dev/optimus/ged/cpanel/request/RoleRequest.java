package br.dev.optimus.ged.cpanel.request;

import br.dev.optimus.ged.cpanel.model.Role;
import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank String permission,
        @NotBlank String description) {

    public Role toRole() {
        return Role.builder()
                .permission(permission.toUpperCase())
                .description(description)
                .build();
    }

}
