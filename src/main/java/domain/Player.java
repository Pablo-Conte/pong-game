package domain;

import java.awt.Color;

public class Player {
    public double x = 0, y = 0;
    public Color color = Color.BLACK;
    public char[] keys = null;
    public boolean left = false, right = false, top = false, down = false;
    public int width = 150, height = 10;
    public int score = 0;

    public Player(double x, double y, Color c, int score, char... keys) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.keys = keys;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}