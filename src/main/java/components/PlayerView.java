package components;

import domain.Player;

import java.awt.Graphics;

public class PlayerView {
    private final Player player;

    public PlayerView(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void draw(Graphics g) {
        g.setColor(player.color);
        g.fillRect((int) player.x, (int) player.y, player.width, player.height);
    }
}