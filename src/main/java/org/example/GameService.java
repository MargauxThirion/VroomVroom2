package org.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class GameService {
    private int positionX = 0;
    private int positionY = 0;
    private final int gridSize = 10; // Taille de la grille 10x10 pour simplifier
    @Inject
    ValidPosition validPosition;

    public boolean move(String direction) {
        int newX = positionX;
        int newY = positionY;

        // Calculer la nouvelle position en fonction de la direction
        switch (direction) {
            case "UP":
                newY--;
                break;
            case "DOWN":
                newY++;
                break;
            case "LEFT":
                newX--;
                break;
            case "RIGHT":
                newX++;
                break;
            default:
                return false;
        }
        // Vérifier si la nouvelle position est valide
        if (canMove(newX, newY)) {
            positionX = newX;
            positionY = newY;
            return true;
        } else {
            return false;
        }
    }

    private boolean canMove(int newX, int newY) {
        if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
            return validPosition.isValidPosition(new Position(newX, newY));
        }
        return false;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public Position getInitialPosition() {
        // Retourne une position initiale prédéfinie ou calculée
        return new Position(1, 0); // Exemple simple, à personnaliser selon vos besoins
    }



}
