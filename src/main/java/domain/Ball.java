package domain;

import java.awt.Color;

public class Ball {
    public int x = 0, y = 0;
    public boolean left = false, right = true, top = false, down = true;
    public int SPEED = 5;
    public int width = 25, height = 25;
    private boolean isMoving = true;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        resetPosition();
    }

    private void resetPosition() {
        x = (800 / 2) - (width);
        y = (600 / 2) - (height);
        left = false;
        right = false;
        top = false;
        down = false;
        isMoving = false;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMovementToTheBall() {
        java.util.Random rand = new java.util.Random();

        if (rand.nextBoolean()) {
            left = true;
            right = false;
        } else {
            left = false;
            right = true;
        }

        if (rand.nextBoolean()) {
            top = true;
            down = false;
        } else {
            top = false;
            down = true;
        }

        isMoving = true;
    }

    public void reset() {
        resetPosition();
    }

    // kept for compatibility with persistence layer
    public Color getColor() {
        return Color.WHITE;
    }
}