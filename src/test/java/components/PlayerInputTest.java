package components;

import org.junit.jupiter.api.Test;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class PlayerInputTest {
    private KeyEvent key(int id, char key) {
        return new KeyEvent(new Canvas(), id, System.currentTimeMillis(), 0,
                KeyEvent.VK_UNDEFINED, key);
    }
    @Test void pressionarESoltarTeclaEsquerdaAtualizaEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'a'));
        assertTrue(player.left);
        player.notifyKeyEvent(key(KeyEvent.KEY_RELEASED, 'a'));
        assertFalse(player.left);
    }
    @Test void pressionarESoltarTeclaDireitaAtualizaEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'd'));
        assertTrue(player.right);
        player.notifyKeyEvent(key(KeyEvent.KEY_RELEASED, 'd'));
        assertFalse(player.right);
    }
    @Test void teclaNaoConfiguradaNaoMoveJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'z'));
        assertFalse(player.left); assertFalse(player.right);
    }
}
