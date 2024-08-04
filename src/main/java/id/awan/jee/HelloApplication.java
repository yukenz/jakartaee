package id.awan.jee;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class HelloApplication extends Application {

    @ApplicationScoped
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}