package domain;

import database.GameDAO;
import database.model.GameSession;
import gamerunner.GameSessionManager;

import java.awt.event.KeyEvent;
import java.util.Objects;

public final class MatchEngine {
    public static final int SCREEN_WIDTH = 800;
    public static final int PLAYER_SPEED = 10;

    private MatchEngine() {
    }

    public static void update(Ball ball, Player player1, Player player2,
                              Line topLine, Line bottomLine, GameSession session) {
        moveBall(ball);
        movePlayer(player1);
        movePlayer(player2);
        clampPlayer(player1);
        clampPlayer(player2);
        resolveCollisions(ball, player1, player2);
        resolveScoring(ball, topLine, bottomLine, player1, player2, session);
    }

    public static void moveBall(Ball ball) {
        if (ball.left) {
            if (ball.x <= 10) {
                ball.left = false;
                ball.right = true;
            }
            ball.x -= ball.SPEED;
        }

        if (ball.right) {
            if (ball.x >= 746) {
                ball.left = true;
                ball.right = false;
            }
            ball.x += ball.SPEED;
        }

        if (ball.top) {
            ball.y -= ball.SPEED;
        }

        if (ball.down) {
            ball.y += ball.SPEED;
        }
    }

    public static void movePlayer(Player player) {
        if (player.left) {
            player.x -= PLAYER_SPEED;
        }

        if (player.right) {
            player.x += PLAYER_SPEED;
        }

        if (player.top) {
            player.y -= PLAYER_SPEED;
        }

        if (player.down) {
            player.y += PLAYER_SPEED;
        }
    }

    public static void clampPlayer(Player player) {
        if (player.x < 0) {
            player.x = 0;
        }
        if (player.x > SCREEN_WIDTH - player.width) {
            player.x = SCREEN_WIDTH - player.width;
        }
    }

    public static void resolveCollisions(Ball ball, Player topPlayer, Player bottomPlayer) {
        int ballTop = ball.y;
        int ballBottom = ball.y + ball.height;

        int topPlayerTop = (int) topPlayer.y;
        int topPlayerBottom = topPlayerTop + topPlayer.height;

        if (ball.top && ballBottom >= topPlayerTop && ballTop <= topPlayerBottom
                && horizontalOverlap(ball, topPlayer)) {
            ball.top = false;
            ball.down = true;
        }

        int bottomPlayerTop = (int) bottomPlayer.y;
        int bottomPlayerBottom = bottomPlayerTop + bottomPlayer.height;

        if (ball.down && ballTop <= bottomPlayerBottom && ballBottom >= bottomPlayerTop
                && horizontalOverlap(ball, bottomPlayer)) {
            ball.top = true;
            ball.down = false;
        }
    }

    private static boolean horizontalOverlap(Ball ball, Player player) {
        int ballLeft = ball.x;
        int ballRight = ball.x + ball.width;
        int playerLeft = (int) player.x;
        int playerRight = playerLeft + player.width;
        return ballRight >= playerLeft && ballLeft <= playerRight;
    }

    public static void resolveScoring(Ball ball, Line topLine, Line bottomLine,
                                      Player player1, Player player2, GameSession session) {
        int ballTop = ball.y;
        int ballBottom = ball.y + ball.height;

        int topLineBottom = topLine.y + topLine.height;
        int bottomLineTop = bottomLine.y;

        if (ballBottom <= topLineBottom) {
            Objects.requireNonNull(session, "session must not be null");
            int actualScore = player2.getScore();
            player2.setScore(actualScore + 1);
            session.setPlayerTwoPoint(session.getPlayerTwoPoint() + 1);
            ball.reset();
        }

        if (ballTop >= bottomLineTop) {
            Objects.requireNonNull(session, "session must not be null");
            int actualScore = player1.getScore();
            player1.setScore(actualScore + 1);
            session.setPlayerOnePoint(session.getPlayerOnePoint() + 1);
            ball.reset();
        }
    }

    // helper to set isMoving false after reset (reset already does it)
    // placeholder removed below

    public static void handleKey(Player player, KeyEvent evt) {
        if (player.keys == null || player.keys.length < 2) {
            return;
        }

        char key0 = Character.toLowerCase(player.keys[0]);
        char key1 = Character.toLowerCase(player.keys[1]);
        char pressed = Character.toLowerCase(evt.getKeyChar());

        switch (evt.getID()) {
            case KeyEvent.KEY_PRESSED:
                if (pressed == key0) {
                    player.left = true;
                } else if (pressed == key1) {
                    player.right = true;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if (pressed == key0) {
                    player.left = false;
                } else if (pressed == key1) {
                    player.right = false;
                }
                break;
            default:
                break;
        }
    }

    public static void restart(GameDAO gameDAO, GameSessionManager gameSessionManager,
                               Ball ball, Player player1, Player player2) {
        GameSession session = gameSessionManager.getSession();
        gameDAO.updateGameSession(session);
        GameSession newSession = gameDAO.createGameSession();
        gameSessionManager.setSession(newSession);

        if (newSession != null) {
            player1.x = newSession.getPlayerOnePosition().x;
            player1.y = newSession.getPlayerOnePosition().y;
            player1.score = newSession.getPlayerOnePoint();
            player2.x = newSession.getPlayerTwoPosition().x;
            player2.y = newSession.getPlayerTwoPosition().y;
            player2.score = newSession.getPlayerTwoPoint();
        }
    }
}