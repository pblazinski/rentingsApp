package pl.lodz.p.edu.grs.jmx;

import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
@ManagedResource
public class MyManageSettings  {

    private int maxThreads = -1;
    private int maxPoolSize = -1;

    @ManagedAttribute(defaultValue = "It's description")
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    @ManagedAttribute
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
}
