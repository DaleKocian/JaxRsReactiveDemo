package io.github.dalekocian;

import org.glassfish.jersey.server.Uri;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by dalek on 6/4/2018.
 */
@Path("reactiveForecast")
public class ForecastReactiveResource {

    @Uri("location")
    private WebTarget locationTarget;

    @Uri("temperature/{city}")
    private WebTarget temperatureTarget;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getLocationsWithTemperature(@Suspended final AsyncResponse async) {
        long startTime = System.currentTimeMillis();
        // Create a stage on retrieving locations
        CompletionStage<List<Location>> locationCS =
                locationTarget.request()
                .rx()
                .get(new GenericType<List<Location>>(){});
        // By composing another stage on the location stage created above, collect the list of forecasts as in one big completion stage
        final CompletionStage<List<Forecast>> forecastCS =
                locationCS.thenCompose(locations -> {
                    // Create a stage for retrieving forecasts as a list of completion stages
                    List<CompletionStage<Forecast>> forecastList =
                    //Stream locations and process each location individually
                    locations.stream().map(location -> {
                       // Create a stage for fetching the temperature value just for one city given by its name
                       final CompletionStage<Temperature> tempCS =
                               temperatureTarget
                               .resolveTemplate("city", location.getName())
                               .request()
                               .rx()
                               .get(Temperature.class);
                       // Then create a completable future that contains an instance of forecast with location and temperature values
                        return CompletableFuture.completedFuture(
                                new Forecast(location))
                                    .thenCombine(tempCS, Forecast::setTemperature);
                    }).collect(Collectors.toList());
                    // Return a final completable future instance when all provided completable futures are completed
                    return CompletableFuture.allOf(
                            forecastList.toArray(
                                    new CompletableFuture[forecastList.size()]))
                                        .thenApply(v -> forecastList.stream()
                                        .map(CompletionStage::toCompletableFuture)
                                                .map(CompletableFuture::join)
                                                .collect(Collectors.toList()));
                });
        CompletableFuture.completedFuture(
                new ServiceResponse())
                .thenCombine(forecastCS,
                        ServiceResponse::forecasts)
                        .whenCompleteAsync((response, throwable) -> {
                            response.setProcessingTime(
                                    System.currentTimeMillis() - startTime);
                            async.resume(response);
                        });
    }
}
