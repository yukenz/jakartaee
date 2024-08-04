package id.awan.jee.service;

import id.awan.jee.model.entity.UsernamePasswordCredential;
import id.awan.jee.util.LoggingManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class CredentialService {

    private Set<UsernamePasswordCredential> usernamePasswordCredentialsSet;

    @PostConstruct
    public void init() {

        usernamePasswordCredentialsSet = new HashSet<>();
        usernamePasswordCredentialsSet.addAll(Set.of(
                new UsernamePasswordCredential("admin", "admin"),
                new UsernamePasswordCredential("user", "user")
        ));

        LoggingManager log = new LoggingManager(this);
        log.logInfo("CredentialService and Content was initialized");
    }

    public void addCredential(UsernamePasswordCredential usernamePasswordCredential) {
        usernamePasswordCredentialsSet.add(usernamePasswordCredential);
    }

    public boolean validateUsernamePasswordCredential(UsernamePasswordCredential usernamePasswordCredential) {

        String username = usernamePasswordCredential.username();
        String password = usernamePasswordCredential.password();

        return usernamePasswordCredentialsSet.stream()
                .anyMatch(credential ->
                        credential.username()
                                .equals(username)
                                && credential.password()
                                .equals(password));

    }
}

