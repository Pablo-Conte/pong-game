package gamerunner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;

import javax.swing.JPanel;
import javax.swing.Timer;

import components.BallView;
import components.LineRicochet;
import components.LineView;
import components.PlayerView;
import database.GameDAO;
import domain.Ball;
import domain.Line;
import domain.MatchEngine;
import domain.Player;
import database.model.GameSession;
import org.postgresql.geometric.PGpoint;

public class GamePanel extends JPanel {
    public static final int SPEED = 10;

    private final GameDAO gameDAO;
    private final GameSessionManager gameSessionManager;

    private final Ball ball;
    private final Player player1;
    private final Player player2;
    private final Line topLine;
    private final Line bottomLine;

    private final BallView ballView;
    private final PlayerView player1View;
    private final PlayerView player2View;
    private final LineView topLineView;
    private final LineView bottomLineView;
    private final LineRicochet leftWall;
    private final LineRicochet rightWall;

    private long startTime;

    private String timeText = "";

    public GamePanel(GameDAO gameDAO, GameSessionManager gameSessionManager) {
        this.gameDAO = gameDAO;
        this.gameSessionManager = gameSessionManager;

        setBackground(Color.DARK_GRAY);
        setFocusable(true);

        this.startTime = System.currentTimeMillis();

        GameSession session = gameSessionManager.getSession();

        int playerOnePoints = session.getPlayerOnePoint();
        int playerTwoPoints = session.getPlayerTwoPoint();
        PGpoint playerOnePosition = session.getPlayerOnePosition();
        PGpoint playerTwoPosition = session.getPlayerTwoPosition();

        this.ball = new Ball(380, 90);
        this.player1 = new Player(playerOnePosition.x, playerOnePosition.y, Color.WHITE, playerOnePoints, new char[] { 'a', 'd' });
        this.player2 = new Player(playerTwoPosition.x, playerTwoPosition.y, Color.WHITE, playerTwoPoints, new char[] { 'j', 'l' });
        this.topLine = new Line(0, 0);
        this.bottomLine = new Line(0, 551);

        this.ballView = new BallView(ball);
        this.player1View = new PlayerView(player1);
        this.player2View = new PlayerView(player2);
        this.topLineView = new LineView(topLine);
        this.bottomLineView = new LineView(bottomLine);
        this.leftWall = new LineRicochet(0, 0);
        this.rightWall = new LineRicochet(774, 0);

        kl = new KeyListener() {
            @Override public void keyTyped(KeyEvent ke) { }

            @Override
            public void keyPressed(KeyEvent ke) {
                boolean ballIsNotMoving = !ball.isMoving();
                if (ke.getKeyChar() == ' ' && ballIsNotMoving) {
                    ball.setMovementToTheBall();
                }

                if (ke.getKeyCode() == KeyEvent.VK_ENTER && ballIsNotMoving) {
                    MatchEngine.restart(gameDAO, gameSessionManager, ball, player1, player2);
                }

                MatchEngine.handleKey(player1, ke);
                MatchEngine.handleKey(player2, ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                MatchEngine.handleKey(player1, ke);
                MatchEngine.handleKey(player2, ke);
            }
        };

        addKeyListener(kl);

        refreshTimer = new Timer(20, (ActionEvent e) -> {
            GameSession currentSession = gameSessionManager.getSession();
            MatchEngine.update(ball, player1, player2, topLine, bottomLine, currentSession);
            repaint();
        });

        refreshElapsedTimer = new Timer(1000, e -> {
            GameSession currentSession = gameSessionManager.getSession();
            currentSession.setElapsedTime(currentSession.getElapsedTime().add(BigInteger.ONE));
            repaint();
        });

        refreshTimer.start();
        refreshElapsedTimer.start();
    }

    private KeyListener kl;
    private Timer refreshTimer;
    private Timer refreshElapsedTimer;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ballView.draw(g);
        player1View.draw(g);
        player2View.draw(g);
        topLineView.draw(g);
        bottomLineView.draw(g);
        leftWall.draw(g);
        rightWall.draw(g);

        long elapsedSeconds = gameSessionManager.getSession().getElapsedTime().longValue();
        timeText = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(g.getFont().deriveFont(30f));
        int textWidth = g.getFontMetrics().stringWidth(timeText);
        int textHeight = g.getFontMetrics().getAscent();
        int x = (getWidth() / 2) - (textWidth / 2) - 5;
        int y = (getHeight() / 2) + (textHeight / 2) - 50;
        g.drawString(timeText, x, y);

        String score1 = "Score: " + gameSessionManager.getSession().getPlayerOnePoint();
        String score2 = "Score: " + gameSessionManager.getSession().getPlayerTwoPoint();

        g.setColor(new Color(230, 230, 230, 50));
        g.setFont(g.getFont().deriveFont(20f));

        g.drawString(score1, (int) player1.x, (int) player1.y + 35);
        g.drawString(score2, (int) player2.x, (int) player2.y + player2.height - 35);

        if (!ball.isMoving()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(25f));

            String msg = "Pressione SPACE para iniciar";
            String msg2 = "Ou ENTER para reiniciar o jogo";
            int helpTextWidth = g.getFontMetrics().stringWidth(msg);
            int xhelpText = (getWidth() - helpTextWidth) / 2;
            int yhelpText = (getHeight() / 2) - 100;
            int helpText2Width = g.getFontMetrics().stringWidth(msg2);
            int xhelpText2 = (getWidth() - helpText2Width) / 2;
            int yhelpText2 = yhelpText + 30;
            g.drawString(msg, xhelpText, yhelpText);
            g.setColor(new Color(230, 230, 230, 50));
            g.drawString(msg2, xhelpText2, yhelpText2);
        }

        GameSession session = gameSessionManager.getSession();
        session.setPlayerOnePosition(new PGpoint((int) player1.x, (int) player1.y));
        session.setPlayerTwoPosition(new PGpoint((int) player2.x, (int) player2.y));

        Toolkit.getDefaultToolkit().sync();
    }
}