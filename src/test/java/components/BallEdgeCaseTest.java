package components;

import database.model.GameSession;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.math.BigInteger;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class BallEdgeCaseTest {
    private GameSession session() {
        return new GameSession(0, 0, 0, null, null, null,
                BigInteger.ZERO, new Timestamp(0));
    }

    // calculateImpact
    @Test void bordaBolaAbaixoDaRaqueteInferiorNaoDeveriaColidir() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 700; ball.top = false; ball.down = true;
        Player superior = new Player(400, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(90, 500, Color.WHITE, 0, 'j', 'l');
        ball.calculateImpact(superior, inferior);
        assertTrue(ball.down, "Possivel defeito: colisao quando a bola ja passou da raquete inferior");
    }

    @Test void bordaBolaEncostandoExatamenteNaBaseDaRaqueteColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 100; ball.y = 20; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        ball.calculateImpact(superior, inferior);
        assertTrue(ball.down);
    }

    @Test void bordaBolaNaQuinaDireitaDaRaqueteColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 240; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        ball.calculateImpact(superior, inferior);
        assertTrue(ball.down);
    }

    @Test void bordaBolaUmPixelAlemDaQuinaNaoColide() {
        Ball ball = new Ball(0, 0);
        ball.x = 241; ball.y = 18; ball.top = true; ball.down = false;
        Player superior = new Player(90, 10, Color.WHITE, 0, 'a', 'd');
        Player inferior = new Player(400, 500, Color.WHITE, 0, 'j', 'l');
        ball.calculateImpact(superior, inferior);
        assertTrue(ball.top);
    }

    // calculateDefeat
    @Test void bordaBolaExatamenteNoLimiteDaLinhaSuperiorPontua() {
        Ball ball = new Ball(0, 0); ball.y = -15;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        ball.calculateDefeat(new LinePoint(0, 0), new LinePoint(0, 590), p1, p2, session());
        assertEquals(1, p2.getScore());
    }

    @Test void bordaBolaUmPixelAntesDaLinhaSuperiorNaoPontua() {
        Ball ball = new Ball(0, 0); ball.y = -14;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        ball.calculateDefeat(new LinePoint(0, 0), new LinePoint(0, 590), p1, p2, session());
        assertEquals(0, p2.getScore());
    }

    @Test void bordaSessaoNulaNaoDeveAlterarPlacarDoJogador() {
        Ball ball = new Ball(0, 0); ball.y = -30;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        try {
            ball.calculateDefeat(new LinePoint(0, 0), new LinePoint(0, 590), p1, p2, null);
        } catch (NullPointerException ignored) { }
        assertEquals(0, p2.getScore(), "Possivel defeito: placar do jogador muda mesmo quando a sessao falha");
    }
}
