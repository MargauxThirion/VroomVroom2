package org.example;



import java.util.ArrayList;
import java.util.List;


public class ValidPosition implements IValidPosition {
    private static final ValidPosition instance = null;

    public static ValidPosition getInstance() {
        if (instance == null) {
            return new ValidPosition();
        }
        return instance;
    }

    private final List<Position> validPositions;

    public ValidPosition() {
        this.validPositions = new ArrayList<>();
        initializeValidPositions();
    }

    private void initializeValidPositions() {
        validPositions.add(new Position(1, 0));
        validPositions.add(new Position(1, 1));
        validPositions.add(new Position(1, 2));
        validPositions.add(new Position(1, 3));
        validPositions.add(new Position(1, 4));
        validPositions.add(new Position(1, 5));
        validPositions.add(new Position(1, 6));
        validPositions.add(new Position(2, 6));
        validPositions.add(new Position(3, 6));
        validPositions.add(new Position(4, 6));
        validPositions.add(new Position(4, 5));
        validPositions.add(new Position(4, 4));
        validPositions.add(new Position(4, 3));
        validPositions.add(new Position(5, 3));
        validPositions.add(new Position(6, 3));
        validPositions.add(new Position(7, 3));
        validPositions.add(new Position(7, 4));
        validPositions.add(new Position(7, 5));
        validPositions.add(new Position(8, 5));
        validPositions.add(new Position(8, 6));
        validPositions.add(new Position(8, 7));
        validPositions.add(new Position(8, 8));
        validPositions.add(new Position(8, 9));
    }
    // Méthode pour obtenir les positions valides
    public List<Position> getValidPositions() {
        return validPositions;
    }

    // Méthode pour vérifier si une position donnée est valide
    public boolean isValidPosition(Position position) {
        return validPositions.contains(position);
    }

    }