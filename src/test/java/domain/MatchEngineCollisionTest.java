package domain;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class MatchEngineCollisionTest {
    @Test void colidirComRaqueteSuperiorFazBolaDescer() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertFalse(ball.top);
        assertTrue(ball.down);
    }

    @Test void colidirComRaqueteInferiorFazBolaSubir() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 480; ball.top = false; ball.down = true;
        Player superior = new Player(400, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(90, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.top);
        assertFalse(ball.down);
    }

    @Test void semSobreposicaoHorizontalNaoInverteDirecao() {
        Ball ball = new Ball(0, 0);
        ball.x = 500; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(90, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.top);
        assertFalse(ball.down);
    }

    @Test void casoDeBordaBolaAcimaDaRaqueteNaoDeveriaColidir() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = -100; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.top, "Possivel defeito: colisao quando a bola esta muito acima da raquete");
    }

    @Test void bordaBolaAbaixoDaRaqueteInferiorNaoDeveriaColidir() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 700; ball.top = false; ball.down = true;
        Player superior = new Player(400, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(90, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.down, "Possivel defeito: colisao quando a bola ja passou da raquete inferior");
    }

    @Test void bordaBolaEncostandoExatamenteNaBaseDaRaqueteColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 20; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.down);
    }

    @Test void bordaBolaNaQuinaDireitaDaRaqueteColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 240; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.down);
    }

    @Test void bordaBolaUmPixelAlemDaQuinaNaoColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 241; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        MatchEngine.resolveCollisions(ball, superior, inferior);
        assertTrue(ball.top);
    }
}