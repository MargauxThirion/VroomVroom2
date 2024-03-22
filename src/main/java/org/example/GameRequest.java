package org.example;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Singleton;
import jakarta.websocket.Session;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class GameRequest {

    private ScheduledFuture<?> elapsedTimeTask;
    private ScheduledExecutorService timer;
    private Instant gameStartTime;
    private boolean gameActive = true;
    private long penaltySeconds = 0;
    private long gameDurationInSeconds = 10;
    private final IGameService gameService;
    private final ValidPosition validPosition;

    public GameRequest() {
        this.gameService = GameService.getInstance();
        this.validPosition = ValidPosition.getInstance();
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public void handleStartGame(Session session) {
        startGame(session);
        Position position = gameService.getInitialPosition();
        session.getAsyncRemote().sendText("Initial position: X=" + position.getX() + ", Y=" + position.getY());
        gameService.setCurrentPosition(position);
        System.out.println("positions vaildes" + validPosition.getValidPositions());
    }

    private void startGame(Session session) {
        penaltySeconds = 0;
        elapsedTimeTask = null;
        gameStartTime = Instant.now();
        gameActive = true;

        Position position = gameService.getInitialPosition();
        gameService.setCurrentPosition(position);

        sendPositions(session, validPosition.getValidPositions());

        elapsedTimeTask = timer.scheduleAtFixedRate(() -> {
            sendElapsedTime(session);
            long elapsedTime = Duration.between(gameStartTime, Instant.now()).toSeconds();
            long totalTime = elapsedTime + penaltySeconds;
            if (totalTime >= gameDurationInSeconds) {
                gameActive = false;
                sendDefeatMessage(session);
                elapsedTimeTask.cancel(false);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    private void sendElapsedTime(Session session) {
        long elapsedTime = Duration.between(gameStartTime, Instant.now()).toSeconds();
        long totalTime = elapsedTime + penaltySeconds;
        try {

            session.getAsyncRemote().sendText("ElapsedTime: " + totalTime + "s");
        } catch (Exception e) {
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
        try {
            session.getAsyncRemote().sendText("Victory: You have won the game!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPositions(Session session, List<Position> positions) {
        JsonArray positionsArray = new JsonArray();
        for (Position position : positions) {
            JsonObject positionObject = new JsonObject()
                    .put("x", position.getX())
                    .put("y", position.getY());
            positionsArray.add(positionObject);
        }
        session.getAsyncRemote().sendText("Path: " + positionsArray.encode());

    }

    public void handleMovement(String direction, Session session) {
        boolean canMove = gameService.move(direction);
        if (gameActive) {
            if (canMove) {
                Position newPosition = new Position(gameService.getPositionX(), gameService.getPositionY());
                gameService.setCurrentPosition(newPosition);
                if (newPosition.getX() == 8 && newPosition.getY() == 9) {
                    informVictory(session);
                } else {
                    session.getAsyncRemote().sendText("New position: X=" + newPosition.getX() + ", Y=" + newPosition.getY());
                }
            } else {
                session.getAsyncRemote().sendText("Penalty: Cannot move in that direction");
                penaltySeconds += 1;
            }} else {
            session.getAsyncRemote().sendText("Game over: Time's up!");
        }
    }

    public void handleEndGame(Session session) {
        if (elapsedTimeTask != null) {
            elapsedTimeTask.cancel(false);
        }
        timer.shutdownNow();
    }
}
