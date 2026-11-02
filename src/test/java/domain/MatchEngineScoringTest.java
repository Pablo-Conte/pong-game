package domain;

import database.model.GameSession;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.math.BigInteger;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class MatchEngineScoringTest {
    private GameSession session() {
        return new GameSession(0, 0, 0, null, null, null,
                BigInteger.ZERO, new Timestamp(0));
    }

    @Test void cruzarLinhaSuperiorPontuaParaJogadorDois() {
        Ball ball = new Ball(0, 0); ball.setMovementToTheBall(); ball.y = -30;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        GameSession session = session();
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        MatchEngine.resolveScoring(ball, top, bottom, p1, p2, session);
        assertEquals(0, p1.getScore()); assertEquals(1, p2.getScore());
        assertEquals(1, session.getPlayerTwoPoint()); assertFalse(ball.isMoving());
    }

    @Test void cruzarLinhaInferiorPontuaParaJogadorUm() {
        Ball ball = new Ball(0, 0); ball.setMovementToTheBall(); ball.y = 590;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        GameSession session = session();
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        MatchEngine.resolveScoring(ball, top, bottom, p1, p2, session);
        assertEquals(1, p1.getScore()); assertEquals(0, p2.getScore());
        assertEquals(1, session.getPlayerOnePoint()); assertFalse(ball.isMoving());
    }

    @Test void bolaDentroDoCampoNaoAlteraPontuacao() {
        Ball ball = new Ball(0, 0); ball.y = 300;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        GameSession session = session();
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        MatchEngine.resolveScoring(ball, top, bottom, p1, p2, session);
        assertEquals(0, p1.getScore()); assertEquals(0, p2.getScore());
        assertEquals(0, session.getPlayerOnePoint()); assertEquals(0, session.getPlayerTwoPoint());
    }

    @Test void bordaBolaExatamenteNoLimiteDaLinhaSuperiorPontua() {
        Ball ball = new Ball(0, 0); ball.y = -15;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        MatchEngine.resolveScoring(ball, top, bottom, p1, p2, session());
        assertEquals(1, p2.getScore());
    }

    @Test void bordaBolaUmPixelAntesDaLinhaSuperiorNaoPontua() {
        Ball ball = new Ball(0, 0); ball.y = -14;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        MatchEngine.resolveScoring(ball, top, bottom, p1, p2, session());
        assertEquals(0, p2.getScore());
    }

    @Test void bordaSessaoNulaNaoDeveAlterarPlacarDoJogador() {
        Ball ball = new Ball(0, 0); ball.y = -30;
        Player p1 = new Player(0, 0, Color.WHITE, 0, 'a', 'd');
        Player p2 = new Player(0, 590, Color.WHITE, 0, 'j', 'l');
        Line top = new Line(0, 0);
        Line bottom = new Line(0, 590);
        try {
            MatchEngine.resolveScoring(ball, top, bottom, p1, p2, null);
        } catch (NullPointerException ignored) { }
        assertEquals(0, p2.getScore(), "Possivel defeito: placar do jogador muda mesmo quando a sessao falha");
    }
}