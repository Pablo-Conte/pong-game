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

import components.Ball;
import components.LinePoint;
import components.LineRicochet;
import components.Player;
import database.GameDAO;
import interfaces.GameInterface;
import interfaces.InteractiveInterface;
import org.postgresql.geometric.PGpoint;

public class GamePanel extends JPanel {
    public static final int SPEED = 10;

    private GameSessionManager gameSessionManager;
    private GameDAO gameDAO;
    private GameInterface[] components;
    private long startTime;

    private String timeText = "";

    public GamePanel(GameDAO gameDAO, GameSessionManager gameSessionManager) {
        this.gameSessionManager = gameSessionManager;
        this.gameDAO = gameDAO;

        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(kl);

        this.startTime = System.currentTimeMillis();

        refreshTimer.start();
        refreshElapsedTimer.start();

        int playerOnePoints = gameSessionManager.getSession().getPlayerOnePoint();
        int playerTwoPoints = gameSessionManager.getSession().getPlayerTwoPoint();
        PGpoint playerOnePosition = gameSessionManager.getSession().getPlayerOnePosition();
        PGpoint playerTwoPosition = gameSessionManager.getSession().getPlayerTwoPosition();

        this.components = new GameInterface[] {
                new Ball(380, 90),
                new Player(playerOnePosition.x, playerOnePosition.y, Color.WHITE, playerOnePoints, new char[] { 'a', 'd' }),
                new Player(playerTwoPosition.x, playerTwoPosition.y, Color.WHITE, playerTwoPoints, new char[] { 'j', 'l' }),
                new LinePoint(0, 0),
                new LinePoint(0, 551),
                new LineRicochet(0, 0),
                new LineRicochet(774, 0)
        };
    }

    KeyListener kl = new KeyListener() {
        @Override public void keyTyped(KeyEvent ke) { }

        @Override
        public void keyPressed(KeyEvent ke) {
            boolean ballIsNotMoving = !((Ball) components[0]).isMoving();
            if (ke.getKeyChar() == ' ' && ballIsNotMoving) {
                ((Ball) components[0]).setMovementToTheBall();
            }

            if (ke.getKeyCode() == KeyEvent.VK_ENTER && ballIsNotMoving) {
                gameDAO.updateGameSession(gameSessionManager.getSession());
                gameSessionManager.setSession(gameDAO.createGameSession());
                ((Player) components[1]).x = gameSessionManager.getSession().getPlayerOnePosition().x;
                ((Player) components[1]).y = gameSessionManager.getSession().getPlayerOnePosition().y;
                ((Player) components[2]).x = gameSessionManager.getSession().getPlayerTwoPosition().x;
                ((Player) components[2]).y = gameSessionManager.getSession().getPlayerTwoPosition().y;
            }


            for (GameInterface gc : components) {
                if (gc instanceof InteractiveInterface) {
                    ((InteractiveInterface) gc).notifyKeyEvent(ke);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            for (GameInterface gc : components) {
                if (gc instanceof InteractiveInterface) {
                    ((InteractiveInterface) gc).notifyKeyEvent(ke);
                }
            }
        }
    };

    Timer refreshTimer = new Timer(20, (ActionEvent e) -> repaint());

    Timer refreshElapsedTimer = new Timer(1000, e -> {
        gameSessionManager.getSession().setElapsedTime(gameSessionManager.getSession().getElapsedTime().add(BigInteger.ONE));
        repaint();
    });

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (GameInterface gc : components) {
            if (gc instanceof Ball) {
                ((Ball) gc).calculateImpact((Player) components[1], (Player) components[2]);
                ((Ball) gc).calculateDefeat((LinePoint) components[3], (LinePoint) components[4],
                        (Player) components[1], (Player) components[2], gameSessionManager.getSession());
            }
            gc.draw(g);

            if (components[1] instanceof Player) {
                Player player1 = (Player) components[1];
                gameSessionManager.getSession().setPlayerOnePosition(new PGpoint((int)player1.x, (int)player1.y));
            }

            if (components[2] instanceof Player) {
                Player player2 = (Player) components[2];
                gameSessionManager.getSession().setPlayerTwoPosition(new PGpoint((int)player2.x, (int)player2.y));
            }
        }

        long elapsedSeconds = gameSessionManager.getSession().getElapsedTime().longValue();
        timeText = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(g.getFont().deriveFont(30f));
        int textWidth = g.getFontMetrics().stringWidth(timeText);
        int textHeight = g.getFontMetrics().getAscent();
        int x = (getWidth() / 2) - (textWidth / 2) - 5;
        int y = (getHeight() / 2) + (textHeight / 2) - 50;
        g.drawString(timeText, x, y);

        Player player2 = (Player) components[2];

        String score1 = "Score: " + gameSessionManager.getSession().getPlayerOnePoint();
        String score2 = "Score: " + gameSessionManager.getSession().getPlayerTwoPoint();

        g.setColor(new Color(230, 230, 230, 50));
        g.setFont(g.getFont().deriveFont(20f));

        int SPACE_BETWEEN_PLAYER_AND_SCORE = 35;
        PGpoint playerOnePosition = gameSessionManager.getSession().getPlayerOnePosition();
        PGpoint playerTwoPosition = gameSessionManager.getSession().getPlayerTwoPosition();
        g.drawString(score1, (int)playerOnePosition.x, (int)playerOnePosition.y + SPACE_BETWEEN_PLAYER_AND_SCORE);
        g.drawString(score2, (int)playerTwoPosition.x, (int)playerTwoPosition.y + player2.height - SPACE_BETWEEN_PLAYER_AND_SCORE);

        Ball ball = (Ball) components[0];
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

        gameSessionManager.getSession().setPlayerOnePosition(new PGpoint((int)playerOnePosition.x, (int)playerOnePosition.y));
        gameSessionManager.getSession().setPlayerTwoPosition(new PGpoint((int)playerTwoPosition.x, (int)playerTwoPosition.y));

        Toolkit.getDefaultToolkit().sync();
    }
}
