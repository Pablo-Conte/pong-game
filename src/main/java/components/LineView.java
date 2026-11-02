package components;

import domain.Line;

import java.awt.Color;
import java.awt.Graphics;

public class LineView {
    private final Line line;

    public LineView(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(line.x, line.y, line.width, line.height);
    }
}