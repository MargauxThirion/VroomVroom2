package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.websocket.Session;
import jakarta.websocket.RemoteEndpoint.Async;
import static org.mockito.Mockito.*;

@QuarkusTest
public class GameRequestTest {
    @Mock
    private Session mockSession;

    @Mock
    private Async mockAsyncRemote;

    private GameRequest gameRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockSession.getAsyncRemote()).thenReturn(mockAsyncRemote);
        gameRequest = new GameRequest();
        gameRequest.handleStartGame(mockSession);
    }

    @Test
    void testHandleStartGame() {
        verify(mockAsyncRemote, times(1)).sendText(contains("Initial position: X="));
    }

    @Test
    void testHandleMovement() {
        gameRequest.handleMovement("UP", mockSession);
        verify(mockAsyncRemote, times(1)).sendText(contains("New position: X=1, Y=1"));
    }


}
