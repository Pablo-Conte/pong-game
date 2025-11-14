package main.java.gamerunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import main.java.components.Ball;
import main.java.components.LinePoint;
import main.java.components.LineRicochet;
import main.java.components.Player;
import main.java.interfaces.GameInterface;
import main.java.interfaces.InteractiveInterface;

public class GamePanel extends JPanel {
    public static final int SPEED = 10;
    private long startTime = System.currentTimeMillis();

    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
    String timeText = String.format("%02d:%02d", elapsed / 60, elapsed % 60);

    GameInterface components[] = {
            new Ball(380, 90),
            new Player(350, 10, Color.white, new char[] { 'a', 'd' }),
            new Player(350, 541, Color.white, new char[] { 'j', 'l' }),
            new LinePoint(0, 0),
            new LinePoint(0, 551),
            new LineRicochet(0, 0),
            new LineRicochet(774, 0)
    };
    KeyListener kl=new KeyListener(){@Override public void keyTyped(KeyEvent ke){return;}

    @Override public void keyPressed(KeyEvent ke){boolean ballIsNotMoving=!((Ball)components[0]).isMoving();if(ke.getKeyChar()==' '&&ballIsNotMoving){if(components[0]instanceof Ball){((Ball)components[0]).setMovementToTheBall();}}

    for(GameInterface gc:components){if(gc instanceof InteractiveInterface){((InteractiveInterface)gc).notifyKeyEvent(ke);}}}

    @Override public void keyReleased(KeyEvent ke){for(GameInterface gc:components){if(gc instanceof InteractiveInterface){((InteractiveInterface)gc).notifyKeyEvent(ke);}}};};

    Timer refreshTimer = new Timer(20, (ActionEvent e) -> {
        repaint();
    });

    public GamePanel() {
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(kl);
        refreshTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (GameInterface gc : components) {
            if (gc instanceof Ball) {
                ((Ball) gc).calculateImpact((Player) components[1], (Player) components[2]);
                ((Ball) gc).calculateDefeat((LinePoint) components[3], (LinePoint) components[4],
                        (Player) components[1], (Player) components[2]);
            }
            gc.draw(g);
        }

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        String timeText = String.format("%02d:%02d", elapsed / 60, elapsed % 60);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(g.getFont().deriveFont(30f));

        int textWidth = g.getFontMetrics().stringWidth(timeText);
        int textHeight = g.getFontMetrics().getAscent();

        int x = (getWidth() / 2) - (textWidth / 2) - 5;
        int y = (getHeight() / 2) + (textHeight / 2) - 50;

        g.drawString(timeText, x, y);

        Player player1 = (Player) components[1];
        Player player2 = (Player) components[2];

        String score1 = "Score: " + player1.getScore();
        String score2 = "Score: " + player2.getScore();

        g.setColor(new Color(230, 230, 230, 50));
        g.setFont(g.getFont().deriveFont(20f));

        int SPACE_BETWEEN_PLAYER_AND_SCORE = 35;

        g.drawString(score1, player1.x, player1.y + SPACE_BETWEEN_PLAYER_AND_SCORE);
        g.drawString(score2, player2.x, player2.y + player2.height - SPACE_BETWEEN_PLAYER_AND_SCORE);

        Ball ball = (Ball) components[0];

        if (!ball.isMoving()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(25f));

            String msg = "Pressione SPACE para iniciar";
            int helpTextWidth = g.getFontMetrics().stringWidth(msg);
            int xhelpText = (getWidth() - helpTextWidth) / 2;
            int yhelpText = (getHeight() / 2) - 100;

            g.drawString(msg, xhelpText, yhelpText);
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
