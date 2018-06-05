package io.github.dalekocian;

/**
 * Created by dalek on 6/4/2018.
 */
public class Forecast {
    private Location location;
    private Temperature temperature;

    public Forecast(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Forecast setTemperature(Temperature temperature) {
        this.temperature = temperature;
        return this;
    }
}
