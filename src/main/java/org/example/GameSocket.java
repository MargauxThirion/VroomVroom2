package org.example;

import jakarta.inject.Inject;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/game-socket")
public class GameSocket {

    @Inject
    IGameService gameService;

    @Inject
    IValidPosition validPosition;

    @OnMessage
    public void onMessage(String message, Session session) {
        if ("GET_INITIAL_POSITION".equals(message)) {
            Position position = gameService.getInitialPosition();
            session.getAsyncRemote().sendText("Initial position: X=" + position.getX() + ", Y=" + position.getY());
        }
        else if ("GET_PATH".equals(message)) {
            System.out.println("positions vaildes"+validPosition.getValidPositions());
            List<Position> positions = validPosition.getValidPositions();
            sendPositions(session, positions);
        }
        else if("UP".equals(message) || "DOWN".equals(message) || "LEFT".equals(message) || "RIGHT".equals(message)){

            boolean canMove = gameService.move(message);
            if (canMove) {
                session.getAsyncRemote().sendText("New position: X=" + gameService.getPositionX() + ", Y=" + gameService.getPositionY());
            } else {
                session.getAsyncRemote().sendText("Cannot move in that direction");
            }
        }
    }

    private void sendPositions(Session session, List<Position> positions) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String positionsJson = objectMapper.writeValueAsString(positions);
            session.getAsyncRemote().sendText("Path: " + positionsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

}
