import java.awt.Color;
import java.awt.Graphics;

public class Ball implements GameComponent {
    int x = 0, y = 0;
    boolean left = false, right = true, top = false, down = true;
    int SPEED = GamePanel.SPEED - 5;
    int width = 25, height = 25;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void calculateDefeat(LinePoint lineTop, LinePoint lineBottom) {
        int lineTopBottom = lineTop.y + lineTop.height;
        int lineBottomTop = lineBottom.y;

        int ballTop = y;
        int ballBottom = y + 25;

        boolean ballPassedLineTop = (ballBottom <= lineTopBottom);

        if (ballPassedLineTop) {
            System.out.println("Player 2 scores a point!");
            resetPosition();
        }

        boolean ballPassedLineBottom = (ballTop >= lineBottomTop);

        if (ballPassedLineBottom) {
            System.out.println("Player 1 scores a point!");
            resetPosition();
        }
    }

    private void resetPosition() {
        x = 380;
        y = 90;
        left = false;
        right = true;
        top = false;
        down = true;
    }
}
