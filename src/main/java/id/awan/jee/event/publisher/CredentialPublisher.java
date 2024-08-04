package id.awan.jee.event.publisher;

import id.awan.jee.model.entity.UsernamePasswordCredential;
import id.awan.jee.util.LoggingManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class CredentialPublisher {

    private LoggingManager log;

    @PostConstruct
    public void init() {
        log = new LoggingManager(this);
    }

    @Inject
    Event<UsernamePasswordCredential> credentialEvent;

    public void publish(UsernamePasswordCredential credential) {
        String username = credential.username();
        String password = credential.password();
        log.logInfo("publishing credential: " + username + " " + password);
        credentialEvent.fireAsync(credential);

    }

}
