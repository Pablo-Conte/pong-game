package database.model;

import org.postgresql.geometric.PGpoint;

public class GameSession {
    private int id;
    private int playerOnePoint;
    private int playerTwoPoint;
    private PGpoint playerOnePosition;
    private PGpoint playerTwoPosition;
    private PGpoint ballPosition;
    private long elapsedMillis;

    public GameSession() {}

    public GameSession(
            int id,
            int playerOnePoint,
            int playerTwoPoint,
            PGpoint playerOnePosition,
            PGpoint playerTwoPosition,
            PGpoint ballPosition,
            long elapsedMillis
    ) {
        this.id = id;
        this.playerOnePoint = playerOnePoint;
        this.playerTwoPoint = playerTwoPoint;
        this.playerOnePosition = playerOnePosition;
        this.playerTwoPosition = playerTwoPosition;
        this.ballPosition = ballPosition;
        this.elapsedMillis = elapsedMillis;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerOnePoint() {
        return playerOnePoint;
    }

    public void setPlayerOnePoint(int playerOnePoint) {
        this.playerOnePoint = playerOnePoint;
    }

    public int getPlayerTwoPoint() {
        return playerTwoPoint;
    }

    public void setPlayerTwoPoint(int playerTwoPoint) {
        this.playerTwoPoint = playerTwoPoint;
    }

    public PGpoint getPlayerOnePosition() {
        return playerOnePosition;
    }

    public void setPlayerOnePosition(PGpoint playerOnePosition) {
        this.playerOnePosition = playerOnePosition;
    }

    public PGpoint getPlayerTwoPosition() {
        return playerTwoPosition;
    }

    public void setPlayerTwoPosition(PGpoint playerTwoPosition) {
        this.playerTwoPosition = playerTwoPosition;
    }

    public PGpoint getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(PGpoint ballPosition) {
        this.ballPosition = ballPosition;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public void setElapsedMillis(long elapsedMillis) {
        this.elapsedMillis = elapsedMillis;
    }
    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", playerOnePoint=" + playerOnePoint +
                ", playerTwoPoint=" + playerTwoPoint +
                ", playerOnePosition=" + pointToStr(playerOnePosition) +
                ", playerTwoPosition=" + pointToStr(playerTwoPosition) +
                ", ballPosition=" + pointToStr(ballPosition) +
                ", elapsedMillis=" + elapsedMillis +
                '}';
    }

    private String pointToStr(PGpoint p) {
        if (p == null) return "null";
        return "(" + p.x + ", " + p.y + ")";
    }
}
