package components;

import gamerunner.GamePanel;
import interfaces.GameInterface;
import interfaces.InteractiveInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player implements GameInterface, InteractiveInterface {
    public double x = 0, y = 0;
    Color color = Color.BLACK;
    char keys[] = null;
    boolean left = false, right = false, top = false, down = false;
    public int width = 150, height = 10;
    int score = 0;

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

    @Override
    public void draw(Graphics g) {
        if (left) {
            x -= GamePanel.SPEED;
        }

        if (right) {
            x += GamePanel.SPEED;
        }

        if (top) {
            y -= GamePanel.SPEED;
        }

        if (down) {
            y += GamePanel.SPEED;
        }

        g.setColor(color);
        g.fillRect((int)x, (int)y, width, height);
    }

    @Override
    public void notifyKeyEvent(KeyEvent evt) {
        switch (evt.getID()) {
            case KeyEvent.KEY_PRESSED:
                if (evt.getKeyChar() == keys[0]) {
                    left = true;
                } else if (evt.getKeyChar() == keys[1]) {
                    right = true;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if (evt.getKeyChar() == keys[0]) {
                    left = false;
                } else if (evt.getKeyChar() == keys[1]) {
                    right = false;
                }
                break;
        }
    }

}
