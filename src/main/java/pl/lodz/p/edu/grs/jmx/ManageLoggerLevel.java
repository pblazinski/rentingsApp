package pl.lodz.p.edu.grs.jmx;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
@ManagedResource
@Slf4j
public class ManageLoggerLevel {

    @Value("${logging.level.}")
    private String loggingLevel;

    @ManagedAttribute
    public String getLoggingLevel() {
        return loggingLevel;
    }

    @ManagedAttribute
    public void setLoggingLevel(final String loggingLevel) {
        setLogLevel(loggingLevel);
        log.debug("This is not seen by info");
        log.info("This is seen by info");
    }

    private void setLogLevel(String logLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (logLevel.equalsIgnoreCase("DEBUG")) {
            loggerContext.getLogger("ROOT").setLevel(Level.DEBUG);
            this.loggingLevel = Level.DEBUG.toString();
        } else if (logLevel.equalsIgnoreCase("INFO")) {
            loggerContext.getLogger("ROOT").setLevel(Level.INFO);
            this.loggingLevel = Level.INFO.toString();
        } else if (logLevel.equalsIgnoreCase("TRACE")) {
            loggerContext.getLogger("ROOT").setLevel(Level.TRACE);
            this.loggingLevel = Level.TRACE.toString();
        }
    }
}
