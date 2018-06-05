package io.github.dalekocian;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalek on 6/4/2018.
 */
@Path("/location")
public class LocationResponse {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("London"));
        locations.add(new Location("Istanbul"));
        locations.add(new Location("Prague"));

        return Response.ok(new GenericEntity<List<Location>>(locations){}).build();
    }
}
