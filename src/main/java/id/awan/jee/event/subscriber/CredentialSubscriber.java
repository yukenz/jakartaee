package id.awan.jee.event.subscriber;

import id.awan.jee.model.entity.UsernamePasswordCredential;
import id.awan.jee.service.CredentialService;
import id.awan.jee.util.LoggingManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@ApplicationScoped
public class CredentialSubscriber {


    private LoggingManager log;

    @PostConstruct
    public void init() {
        log = new LoggingManager(this);
    }

    @Inject
    CredentialService credentialService;

    public void subscribe(
            @ObservesAsync UsernamePasswordCredential usernamePasswordCredential
    ) {
        log.logInfo("Try to add credential: " + usernamePasswordCredential);
        try {
            Thread.sleep(2000);
            credentialService.addCredential(usernamePasswordCredential);
            log.logInfo("Credential added: " + usernamePasswordCredential);
        } catch (InterruptedException e) {
            log.logInfo("Credential addition failed: " + usernamePasswordCredential);
        }
    }
}
