import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel{
    public static final int SPEED = 10;
    KeyListener kl = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent ke) {
            System.out.println("TYPED: " + ke.getKeyChar());
        }
        @Override
        public void keyPressed(KeyEvent ke) {
            System.out.println("PRESSED: " + ke.getKeyChar());
            for(GameComponent gc:components){
                if(gc instanceof InteractiveComponent){
                    ((InteractiveComponent)gc).notifyKeyEvent(ke);
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent ke) {
            System.out.println("PRESSED: " + ke.getKeyChar());
            for(GameComponent gc:components){
                if(gc instanceof InteractiveComponent){
                    ((InteractiveComponent)gc).notifyKeyEvent(ke);
                }
            }
        };
    };

    GameComponent components[] = {
        new SquareComponent(380, 90),
        new SmallBall(400, 220, Color.YELLOW, new char[]{'a','d', 'w', 's'})
    };
    Timer t = new Timer(20, (ActionEvent e) -> {

        repaint();
    });
    
    public GamePanel() {
        setBackground(Color.white);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(kl);
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(GameComponent gc:components){
            gc.draw(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
}

