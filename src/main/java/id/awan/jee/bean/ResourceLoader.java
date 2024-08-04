package id.awan.jee.bean;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;

@ApplicationScoped
public class ResourceLoader {

    private final ClassLoader classLoader;

    public ResourceLoader() {
        this.classLoader = getClass()
                .getClassLoader();
    }

    public byte[] loadResourceAsBytes(String resourcePath) {
        try {
            return classLoader.getResourceAsStream(resourcePath)
                    .readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
