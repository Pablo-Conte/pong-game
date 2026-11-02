package components;

import domain.Ball;

import java.awt.Color;
import java.awt.Graphics;

public class BallView {
    private final Ball ball;

    public BallView(Ball ball) {
        this.ball = ball;
    }

    public Ball getBall() {
        return ball;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
    }
}