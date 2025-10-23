package components;
import java.awt.Color;
import java.awt.Graphics;

import interfaces.GameInterface;

public class LinePoint implements GameInterface {
    public int x = 0, y = 0;
    public int width = 800, height = 10;

    public LinePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }
}
