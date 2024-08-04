package id.awan.jee.model.entity;

import id.awan.jee.validation.address.ValidAddress;
import jakarta.validation.constraints.NotNull;

@ValidAddress
public record Address(
        String street,
        String city,
        String province,
        @NotNull
        String country,
        String postalCode
) {
}
