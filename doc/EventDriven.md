# Buat Bean yang bertugas sebagai publisher

```java

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
        credentialEvent.fireAsync(new UsernamePasswordCredential(username, password));

    }

}
```

# Buat Bean yang bertugas sebagai subscriber

````java
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
````