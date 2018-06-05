package io.github.dalekocian;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dalek on 6/4/2018.
 */
@ApplicationPath("resources")
public class MyApplication {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ForecastResource.class);
        classes.add(TemperatureResource.class);
        classes.add(LocationResource.class);
        classes.add(ForecastReactiveResource.class);
        return classes;
    }
}
