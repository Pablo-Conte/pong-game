import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    public static final int SPEED = 10;

    GameComponent components[] = {
            new Ball(380, 90),
            new Player(350, 10, Color.white, new char[] { 'a', 'd' }),
            new Player(350, 541, Color.white, new char[] { 'j', 'l' }),
            new LinePoint(0, 0),
            new LinePoint(0, 551),
            new LineRicochet(0, 0),
            new LineRicochet(774, 0)
    };
    KeyListener kl = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent ke) {
            return;
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            for (GameComponent gc : components) {
                if (gc instanceof InteractiveComponent) {
                    ((InteractiveComponent) gc).notifyKeyEvent(ke);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            for (GameComponent gc : components) {
                if (gc instanceof InteractiveComponent) {
                    ((InteractiveComponent) gc).notifyKeyEvent(ke);
                }
            }
        };
    };

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

        for (GameComponent gc : components) {
            if (gc instanceof Ball) {
                ((Ball) gc).calculateImpact((Player) components[1], (Player) components[2]);
                ((Ball) gc).calculateDefeat((LinePoint) components[3], (LinePoint) components[4], (Player) components[1], (Player) components[2]);
            }
            gc.draw(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
