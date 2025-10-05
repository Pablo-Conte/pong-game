import java.awt.Color;
import java.awt.Graphics;

public class LineRicochet implements GameComponent {
    int x = 0, y = 0;

    public LineRicochet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(x, y, 10, 600);
    }
}
