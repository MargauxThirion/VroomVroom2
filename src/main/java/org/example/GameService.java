package org.example;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GameService implements IGameService {

    private static GameService instance;

    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }
    Position currentPosition;
    @PostConstruct
    public void init() {
        currentPosition = getInitialPosition(); // Assurez-vous que currentPosition n'est jamais null
    }

    public void setCurrentPosition(Position position){
        this.currentPosition = position;
    }

    public Position getCurrentPositions(){
        return currentPosition;
    }
    private final int gridSize = 10; // Taille de la grille 10x10 pour simplifier
    @Inject
    ValidPosition validPosition;

    public boolean move(String direction) {
        if (currentPosition == null) {
            currentPosition = getInitialPosition();
        }
        int newX = currentPosition.getX();
        int newY = currentPosition.getY();
        System.out.println("position actuelle: "+newX+" "+newY);

        // Calculer la nouvelle position en fonction de la direction
        switch (direction) {
            case "UP":
                newY++;
                break;
            case "DOWN":
                newY--;
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
        System.out.println("newX: "+newX+" newY: "+newY);
        if (canMove(newX, newY)) {
            currentPosition = new Position(newX, newY);
            return true;
        } else {
            return false;
        }
    }

    private boolean canMove(int newX, int newY) {
        if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
            validPosition = ValidPosition.getInstance();
            return validPosition.isValidPosition(new Position(newX, newY));
        }
        return false;
    }

    public int getPositionX() {
        return currentPosition.getX();
    }

    public int getPositionY() {
        return currentPosition.getY();
    }

    public Position getInitialPosition() {
        return new Position(1, 0);
    }

}
