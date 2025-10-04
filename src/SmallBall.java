import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class SmallBall implements GameComponent, InteractiveComponent{
    int x = 0, y = 0;
    Color color = Color.BLACK;
    char keys[] = null;
    boolean left = false, right = false, top = false, down = false;

    public SmallBall(int x, int y, Color c, char... keys){
        this.x = x;
        this.y = y;
        this.color = c;
        this.keys = keys;
    }
    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        if(left){
            x -= GamePanel.SPEED;
        }

        if(right){
            x += GamePanel.SPEED;
        }

        if(top){
            y -= GamePanel.SPEED;
        }

        if(down){
            y += GamePanel.SPEED;
        }
        
        g.setColor(color);
        g.fillOval(x, y, 20, 20);
        g.fillOval(x-10, y+20, 40, 40);
    }
    @Override
    public void notifyKeyEvent(KeyEvent evt) {
        switch (evt.getID()) {
            case KeyEvent.KEY_PRESSED:
                if(evt.getKeyChar() == keys[0]){
                    left = true;
                }else if(evt.getKeyChar() == keys[1]){
                    right = true;
                }else if(evt.getKeyChar() == keys[2]){
                    top = true;
                }else if(evt.getKeyChar() == keys[3]){
                    down = true;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if(evt.getKeyChar() == keys[0]){
                    left = false;
                }else if(evt.getKeyChar() == keys[1]){
                    right = false;
                }else if(evt.getKeyChar() == keys[2]){
                    top = false;
                }else if(evt.getKeyChar() == keys[3]){
                    down = false;
                }
                break;
        }
    }
    
}
