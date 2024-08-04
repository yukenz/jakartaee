package id.awan.jee.util;


import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingManager {

    private final Logger logger;

    public LoggingManager(Object object) {
        Class<?> clazz = object.getClass();
        logger = Logger.getLogger(clazz.getName());
        this.logInfo("Bean successfully initialized");
    }


    public void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public void logSevere(String message) {
        logger.log(Level.SEVERE, message);
    }

    public void logAll(String message) {
        logger.log(Level.ALL, message);
    }

}