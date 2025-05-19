package com.mygame.model;

public class GameModelContext {
    private final float leftBorderCoordinate;
    private final float rightBorderCoordinate;

    public GameModelContext(float leftBorderCoordinate, float rightBorderCoordinate)
    {
        this.leftBorderCoordinate = leftBorderCoordinate;
        this.rightBorderCoordinate = rightBorderCoordinate;
    }

    public float getLeftBorderCoordinate() {
        return leftBorderCoordinate;
    }

    public float getRightBorderCoordinate() {
        return rightBorderCoordinate;
    }
}
