package database.model;

import org.postgresql.geometric.PGpoint;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;


public class GameSession {
    private int id;
    private int playerOnePoint;
    private int playerTwoPoint;
    private PGpoint playerOnePosition;
    private PGpoint playerTwoPosition;
    private PGpoint ballPosition;
    private BigInteger elapsedTime;
    private Timestamp createdAt;

    public GameSession(
            int id,
            int playerOnePoint,
            int playerTwoPoint,
            PGpoint playerOnePosition,
            PGpoint playerTwoPosition,
            PGpoint ballPosition,
            BigInteger elapsedTime,
            Timestamp createdAt
    ) {
        this.id = id;
        this.playerOnePoint = playerOnePoint;
        this.playerTwoPoint = playerTwoPoint;
        this.playerOnePosition = playerOnePosition;
        this.playerTwoPosition = playerTwoPosition;
        this.ballPosition = ballPosition;
        this.elapsedTime = elapsedTime;
        this.createdAt = createdAt;

    }

    public BigInteger getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(BigInteger elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", playerOnePoint=" + playerOnePoint +
                ", playerTwoPoint=" + playerTwoPoint +
                ", playerOnePosition=" + pointToStr(playerOnePosition) +
                ", playerTwoPosition=" + pointToStr(playerTwoPosition) +
                ", ballPosition=" + pointToStr(ballPosition) +
                ", createdAt=" + createdAt.toString() +
                '}';
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    private String pointToStr(PGpoint p) {
        if (p == null) return "null";
        return "(" + p.x + ", " + p.y + ")";
    }
}
