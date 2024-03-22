package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = GameService.getInstance();
        gameService.init();
    }

    @Test
    void testInitialPosition() {
        Position initialPosition = gameService.getCurrentPositions();
        assertEquals(1, initialPosition.getX());
        assertEquals(0, initialPosition.getY());
    }

    @Test
    void testMoveValid() {
        System.out.println("testMove: UP");
        assertTrue(gameService.move("UP"));
        assertEquals(1, gameService.getCurrentPositions().getX());
        assertEquals(1, gameService.getCurrentPositions().getY());

        System.out.println("testMove: DIAGONAL");
        assertFalse(gameService.move("DIAGONAL"));
    }

    @Test
    void testMoveToInvalidPosition() {
        System.out.println("testMove: RIGHT");
        boolean result = gameService.move("RIGHT");
        assertFalse(result);
        // On verifie que la position n'a pas change
        assertEquals(1, gameService.getCurrentPositions().getX());
        assertEquals(0, gameService.getCurrentPositions().getY());
    }

}
