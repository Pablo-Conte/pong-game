package components;

import database.model.GameSession;
import gamerunner.GamePanel;
import interfaces.GameInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class Ball implements GameInterface {
    int x = 0, y = 0;
    boolean left = false, right = true, top = false, down = true;
    int SPEED = GamePanel.SPEED - 5;
    int width = 25, height = 25;
    private boolean isMoving = true;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        resetPosition();
    }

    @Override
    public void draw(Graphics g) {
        if (left) {
            if (x <= 10) {
                left = false;
                right = true;
            }
            x -= SPEED;
        }

        if (right) {
            if (x >= 746) {
                left = true;
                right = false;
            }
            x += SPEED;
        }

        if (top) {
            y -= SPEED;
        }

        if (down) {
            y += SPEED;
        }

        g.setColor(Color.white);
        g.fillOval(x, y, width, height);
    }

    private boolean areWeInTheSameWidthArea(Player player) {
        int playerX = player.x;
        int playerWidth = player.width;
        int ballX = x;
        int ballWidth = width;

        if (ballX + ballWidth >= playerX && ballX <= playerX + playerWidth) {
            return true;
        }

        return false;
    }

    public void calculateImpact(Player player1, Player player2) {
        int player1Bottom = player1.y + player1.height;
        int player2Top = player2.y;

        int ballTop = y;
        int ballBottom = y + 25;

        boolean ballImpactedOnPlayer1Bottom = (ballTop <= player1Bottom);

        if (ballImpactedOnPlayer1Bottom && areWeInTheSameWidthArea(player1)) {
            top = false;
            down = true;
        }

        boolean ballImpactedOnPlayer2Top = (ballBottom >= player2Top);
        if (ballImpactedOnPlayer2Top && areWeInTheSameWidthArea(player2)) {
            top = true;
            down = false;
        }
    }

    public void calculateDefeat(LinePoint lineTop, LinePoint lineBottom, Player player1, Player player2, GameSession gameSession) {
        int lineTopBottom = lineTop.y + lineTop.height;
        int lineBottomTop = lineBottom.y;

        int ballTop = y;
        int ballBottom = y + 25;

        boolean ballPassedLineTop = (ballBottom <= lineTopBottom);

        if (ballPassedLineTop) {
            int actualScore = player2.getScore();
            player2.setScore(actualScore + 1);
            gameSession.setPlayerTwoPoint(gameSession.getPlayerTwoPoint() + 1);
            resetPosition();
        }

        boolean ballPassedLineBottom = (ballTop >= lineBottomTop);

        if (ballPassedLineBottom) {
            int actualScore = player1.getScore();
            player1.setScore(actualScore + 1);
            gameSession.setPlayerOnePoint(gameSession.getPlayerOnePoint() + 1);
            resetPosition();
        }
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
        Random rand = new Random();

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
}
