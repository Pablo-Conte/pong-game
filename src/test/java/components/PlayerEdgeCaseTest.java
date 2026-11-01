package components;

import org.junit.jupiter.api.Test;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

class PlayerEdgeCaseTest {
    private KeyEvent key(int id, char key) {
        return new KeyEvent(new Canvas(), id, System.currentTimeMillis(), 0,
                KeyEvent.VK_UNDEFINED, key);
    }

    // notifyKeyEvent
    @Test void bordaJogadorSemTeclasNaoDeveLancarExcecao() {
        Player player = new Player(100, 20, Color.WHITE, 0);
        assertDoesNotThrow(() -> player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'a')),
                "Possivel defeito: jogador sem teclas configuradas quebra ao receber evento");
    }

    @Test void bordaTeclaMaiusculaComCapsLockDeveriaMoverJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'A'));
        assertTrue(player.left, "Possivel defeito: com Caps Lock ligado o jogador nao se move");
    }

    @Test void bordaEventoKeyTypedNaoMoveJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_TYPED, 'a'));
        assertFalse(player.left);
    }

    @Test void bordaSoltarTeclaSemTerPressionadoNaoAlteraEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_RELEASED, 'a'));
        assertFalse(player.left); assertFalse(player.right);
    }

    // draw: posicao negativa
    @Test void bordaRaqueteNaoDeveSairDaTelaPelaEsquerda() {
        Player player = new Player(0, 20, Color.WHITE, 0, 'a', 'd');
        player.notifyKeyEvent(key(KeyEvent.KEY_PRESSED, 'a'));
        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB).getGraphics();
        for (int i = 0; i < 5; i++) player.draw(g);
        assertTrue(player.x >= 0, "Possivel defeito: raquete com x negativo (" + player.x + ") sai da tela");
    }
}
