package org.example;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;

import java.util.Map;

@ServerEndpoint("/game-socket")
public class GameSocket {
    @Inject
    GameRequest gameRequest;
    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.equals("START")) {
            gameRequest.handleStartGame(session);

        } else if (message.equals("UP") || message.equals("DOWN") || message.equals("LEFT") || message.equals("RIGHT")) {
            gameRequest.handleMovement(message, session);
        }
    }

}
