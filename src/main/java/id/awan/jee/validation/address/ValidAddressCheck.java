package id.awan.jee.validation.address;

import id.awan.jee.model.entity.Address;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAddressCheck implements ConstraintValidator<ValidAddress, Address> {

    @Override
    public boolean isValid(Address value, ConstraintValidatorContext context) {

        if (value.postalCode().startsWith("0000")) {
            return false;
        }

        return true;
    }
}
