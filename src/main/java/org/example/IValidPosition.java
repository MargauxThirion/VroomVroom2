package org.example;

import java.util.List;

public interface IValidPosition {
    boolean isValidPosition(Position position);

    List<Position> getValidPositions();

}
