package id.awan.jee.service;

import id.awan.jee.util.LoggingManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class UserService {

    List<String> usernames;

    @PostConstruct
    void postConstruct() {
        LoggingManager log = new LoggingManager(this);
        usernames = List.of("awan", "budi", "cici");
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
