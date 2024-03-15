package org.example;


public interface IGameService {

    boolean move(String direction);
    Position getInitialPosition();
    int getPositionX();
    int getPositionY();

}
