package id.awan.jee.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

@RequestScoped
public class ValidationService {

    @Inject
    private ValidatorFactory validatorFactory;

    public String getViolationMessages(Object object) {

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(violation -> {

                        String violationSource = violation.getPropertyPath().toString();

                        // Jika blank maka Level class yang invalid
                        // Jika berisi maka level Field, lanjutkan
                        if (violationSource.isBlank()) {
                            violationSource = "Entity " + violation.getInvalidValue()
                                    .getClass().getSimpleName();

                        } else {
                            violationSource = "Field " + violationSource;
                        }

                        // [field/entity] : [message]
                        return String.format("[%s] : %s",
                                violationSource,
                                violation.getMessage());

                    })
                    .collect(Collectors.joining(", "));
        } else {
            return null;
        }

    }

}
