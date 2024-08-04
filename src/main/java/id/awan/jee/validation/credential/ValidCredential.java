package id.awan.jee.validation.credential;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CredentialValidator.class)
public @interface ValidCredential {

    String message() default "Invalid Credential";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
