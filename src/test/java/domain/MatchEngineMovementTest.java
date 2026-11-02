package domain;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class MatchEngineMovementTest {
    private KeyEvent key(int id, char keyChar) {
        return new KeyEvent(new java.awt.Canvas(), id, System.currentTimeMillis(), 0,
                KeyEvent.VK_UNDEFINED, keyChar);
    }

    @Test void bordaRaqueteNaoDeveSairDaTelaPelaEsquerda() {
        Player player = new Player(0, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'a'));
        for (int i = 0; i < 5; i++) {
            MatchEngine.movePlayer(player);
            MatchEngine.clampPlayer(player);
        }
        assertTrue(player.x >= 0, "Possivel defeito: raquete com x negativo (" + player.x + ") sai da tela");
    }
}