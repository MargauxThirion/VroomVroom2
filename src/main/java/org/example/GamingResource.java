package org.example;

import jakarta.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/game")
public class GamingResource {

    @Inject
    IGameService gameService;

    @POST
    @Path("/move")
    public Response move(@QueryParam("direction") String direction) {
        boolean canMove = gameService.move(direction);
        if (canMove) {
            return Response.ok().entity("New position: X=" + gameService.getPositionX() + ", Y=" + gameService.getPositionY()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot move in that direction").build();
        }
    }

    @GET
    @Path("/initialPosition")
    public Response getInitialPosition() {
        Position position = gameService.getInitialPosition();
        return Response.ok().entity("Initial position: X=" + position.getX() + ", Y=" + position.getY()).build();
    }
}
