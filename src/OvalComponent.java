import java.awt.Color;
import java.awt.Graphics;

public class OvalComponent implements GameComponent{
    int x = 0, y = 0;

    public OvalComponent(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fillOval(x, y, 100, 100);
    }
}
