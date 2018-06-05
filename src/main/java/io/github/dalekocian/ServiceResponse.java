package io.github.dalekocian;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalek on 6/4/2018.
 */
public class ServiceResponse {
    private long processingTime;
    private List<Forecast> forecasts = new ArrayList<>();

    public ServiceResponse() {
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public ServiceResponse forecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
        return this;
    }
}
