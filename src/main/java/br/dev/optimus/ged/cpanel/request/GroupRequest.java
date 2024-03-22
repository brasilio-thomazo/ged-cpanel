package br.dev.optimus.ged.cpanel.request;

import java.util.HashSet;
import java.util.Set;

import br.dev.optimus.ged.cpanel.model.Group;
import jakarta.validation.constraints.NotBlank;

public record GroupRequest(
        @NotBlank String name,
        @NotBlank String description,
        Set<Long> roles) {

    public Group toGroup() {
        return Group.builder()
                .name(name.toUpperCase())
                .description(description)
                .roles(new HashSet<>())
                .build();
    }
}
