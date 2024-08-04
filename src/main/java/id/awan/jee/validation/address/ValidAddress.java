package id.awan.jee.validation.address;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidAddressCheck.class)
public @interface ValidAddress {

    String message() default "Invalid address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
