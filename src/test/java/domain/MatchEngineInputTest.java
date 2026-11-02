package domain;

import org.junit.jupiter.api.Test;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class MatchEngineInputTest {
    private KeyEvent key(int id, char key) {
        return new KeyEvent(new Canvas(), id, System.currentTimeMillis(), 0,
                KeyEvent.VK_UNDEFINED, key);
    }

    @Test void pressionarESoltarTeclaEsquerdaAtualizaEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'a'));
        assertTrue(player.left);
        MatchEngine.handleKey(player, key(KeyEvent.KEY_RELEASED, 'a'));
        assertFalse(player.left);
    }

    @Test void pressionarESoltarTeclaDireitaAtualizaEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'd'));
        assertTrue(player.right);
        MatchEngine.handleKey(player, key(KeyEvent.KEY_RELEASED, 'd'));
        assertFalse(player.right);
    }

    @Test void teclaNaoConfiguradaNaoMoveJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'z'));
        assertFalse(player.left); assertFalse(player.right);
    }

    @Test void bordaJogadorSemTeclasNaoDeveLancarExcecao() {
        Player player = new Player(100, 20, Color.WHITE, 0);
        assertDoesNotThrow(() -> MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'a')),
                "Possivel defeito: jogador sem teclas configuradas quebra ao receber evento");
    }

    @Test void bordaTeclaMaiusculaComCapsLockDeveriaMoverJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_PRESSED, 'A'));
        assertTrue(player.left, "Possivel defeito: com Caps Lock ligado o jogador nao se move");
    }

    @Test void bordaEventoKeyTypedNaoMoveJogador() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_TYPED, 'a'));
        assertFalse(player.left);
    }

    @Test void bordaSoltarTeclaSemTerPressionadoNaoAlteraEstado() {
        Player player = new Player(100, 20, Color.WHITE, 0, 'a', 'd');
        MatchEngine.handleKey(player, key(KeyEvent.KEY_RELEASED, 'a'));
        assertFalse(player.left); assertFalse(player.right);
    }
}