package org.example;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/game-socket")
public class GameSocket {
    private ScheduledFuture<?> elapsedTimeTask;
    IGameService gameService = GameService.getInstance();
    ValidPosition validPosition = ValidPosition.getInstance();
    private ScheduledExecutorService timer;
    private Instant gameStartTime;
    private boolean gameActive = true;
    private long penaltySeconds = 0;


    public GameSocket() {
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        gameService = GameService.getInstance();
        validPosition = ValidPosition.getInstance();
        if (message.equals("START")) {
            startGame(session);
            Position position = gameService.getInitialPosition();
            session.getAsyncRemote().sendText("Initial position: X=" + position.getX() + ", Y=" + position.getY());
            gameService.setCurrentPosition(position);
            validPosition = ValidPosition.getInstance();
            System.out.println("positions vaildes" + validPosition.getValidPositions());
            List<Position> positions = validPosition.getValidPositions();

        } else if (message.equals("UP") || message.equals("DOWN") || message.equals("LEFT") || message.equals("RIGHT")) {
                boolean canMove = gameService.move(message);
                if (gameActive) {
                    if (canMove) {
                        Position newPosition = new Position(gameService.getPositionX(), gameService.getPositionY());
                        gameService.setCurrentPosition(newPosition);
                        if (newPosition.getX() == 8 && newPosition.getY() == 9) {
                            informVictory(session);
                        } else {
                            session.getAsyncRemote().sendText("Nouvelle position: X=" + newPosition.getX() + ", Y=" + newPosition.getY());
                        }
                    } else {
                        session.getAsyncRemote().sendText("Penalty: Cannot move in that direction");
                        penaltySeconds += 1;
                    }} else {
                    session.getAsyncRemote().sendText("Game over: Time's up!");
                }
        }
    }

    private void startGame(Session session) {
        gameStartTime = Instant.now();
        gameActive = true;

        Position position = gameService.getInitialPosition();
        gameService.setCurrentPosition(position);

        sendPositions(session, validPosition.getValidPositions());

        timer.schedule(() -> {
            gameActive = false;
            sendDefeatMessage(session);
        }, 10, TimeUnit.SECONDS);

        elapsedTimeTask = timer.scheduleAtFixedRate(() -> {
            sendElapsedTime(session);
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void sendElapsedTime(Session session) {
        long elapsedTime = Duration.between(gameStartTime, Instant.now()).toSeconds() + penaltySeconds;
        try {
            session.getAsyncRemote().sendText("ElapsedTime: " + elapsedTime + "s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleMovement(String direction, Session session) {
        if (gameService.move(direction)) {
            Position newPosition = gameService.getCurrentPositions();

            if (newPosition.getX() == 8 && newPosition.getY() == 9) {
                informVictory(session);
            } else {
                try {
                    session.getAsyncRemote().sendText(String.format("New position: X=%d, Y=%d", newPosition.getX(), newPosition.getY()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                session.getAsyncRemote().sendText("Cannot move in that direction");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPositions(Session session, List<Position> positions) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String positionsJson = objectMapper.writeValueAsString(positions);
            session.getAsyncRemote().sendText("Path: " + positionsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendDefeatMessage(Session session) {
        if (elapsedTimeTask != null) {
            elapsedTimeTask.cancel(false);
        }
        try {
            session.getAsyncRemote().sendText("Defeat: Time's up!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void informVictory(Session session) {
        if (elapsedTimeTask != null) {
            elapsedTimeTask.cancel(false);
        }
        timer.shutdownNow();
        try {
            session.getAsyncRemote().sendText("Victory: You have won the game!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
