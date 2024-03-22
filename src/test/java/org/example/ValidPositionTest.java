package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ValidPositionTest {

    @Test
    void testInitializeValidPositions() {
        ValidPosition validPosition = ValidPosition.getInstance();
        List<Position> positions = validPosition.getValidPositions();

        assertEquals(23, positions.size());

        assertTrue(positions.contains(new Position(1, 0)));
        assertTrue(positions.contains(new Position(8, 9)));
        assertFalse(positions.contains(new Position(0, 0)));
    }

    @Test
    void testIsValidPosition() {
        ValidPosition validPosition = ValidPosition.getInstance();

        // Positions valides
        assertTrue(validPosition.isValidPosition(new Position(1, 0)));
        assertTrue(validPosition.isValidPosition(new Position(8, 9)));

        // Positions invalides
        assertFalse(validPosition.isValidPosition(new Position(0, 0)));
        assertFalse(validPosition.isValidPosition(new Position(10, 10)));
    }

}
