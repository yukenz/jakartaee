package id.awan.jee.validation.credential;

import id.awan.jee.model.entity.Credential;
import id.awan.jee.model.entity.UsernamePasswordCredential;
import id.awan.jee.service.CredentialService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CredentialValidator implements ConstraintValidator<ValidCredential, Credential> {

    @Inject
    CredentialService credentialService;

    @Override
    public boolean isValid(Credential value, ConstraintValidatorContext context) {

        if (value instanceof UsernamePasswordCredential upc) {
            return credentialService
                    .validateUsernamePasswordCredential(upc);
        }

        return false;

    }
}
