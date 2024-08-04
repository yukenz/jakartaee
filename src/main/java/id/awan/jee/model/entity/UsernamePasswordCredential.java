package id.awan.jee.model.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsernamePasswordCredential(
        @NotNull
        @NotBlank
        String username,
        @NotNull
        @NotBlank
        String password) implements Credential {
}
