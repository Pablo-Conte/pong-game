package database;

import database.model.GameSession;
import org.postgresql.geometric.PGpoint;

import java.math.BigInteger;
import java.sql.*;
import java.util.Properties;

public class GameDAO {
    private Properties props;

    public GameDAO(Properties props) {
        this.props = props;
    }

    public GameSession initOrReloadGame() {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        String db_url = props.getProperty("db.url");

        try (Connection conn = DriverManager.getConnection(db_url, user, pass)) {

            String sql = """
                SELECT *
                FROM GameSession
                ORDER BY createdAt DESC
                LIMIT 1
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return this.createGameSession();
                }

                int id = rs.getInt("id");
                int p1 = rs.getInt("playerOnePoint");
                int p2 = rs.getInt("playerTwoPoint");

                PGpoint p1Pos = new PGpoint(rs.getDouble("playerOnePositionX"), rs.getDouble("playerOnePositionY"));
                PGpoint p2Pos = new PGpoint(rs.getDouble("playerTwoPositionX"), rs.getDouble("playerTwoPositionY"));
                PGpoint ballPos = new PGpoint(rs.getDouble("ballPositionX"), rs.getDouble("ballPositionY"));

                BigInteger elapsedTime = rs.getBigDecimal("elapsedTime").toBigInteger();
                Timestamp createdAt = rs.getTimestamp("createdAt");

                return new GameSession(id, p1, p2, p1Pos, p2Pos, ballPos, elapsedTime, createdAt);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameSession createGameSession() {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        String db_url = props.getProperty("db.url");

        String sql = "INSERT INTO GameSession DEFAULT VALUES";

        try (Connection conn = DriverManager.getConnection(db_url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    return null;
                }
                int id = keys.getInt(1);

                try (PreparedStatement select = conn.prepareStatement(
                        "SELECT elapsedTime, createdAt FROM GameSession WHERE id = ?")) {
                    select.setInt(1, id);
                    ResultSet rs = select.executeQuery();
                    if (!rs.next()) {
                        return null;
                    }

                    BigInteger elapsedTime = rs.getBigDecimal("elapsedTime").toBigInteger();
                    Timestamp createdAt = rs.getTimestamp("createdAt");
                    return new GameSession(
                            id,
                            0,
                            0,
                            new PGpoint(350, 10),
                            new PGpoint(350, 541),
                            new PGpoint(380, 90),
                            elapsedTime,
                            createdAt
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateGameSession(GameSession session) {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        String db_url = props.getProperty("db.url");

        String sql = """
            UPDATE GameSession
            SET
                playerOnePoint = ?,
                playerTwoPoint = ?,
                playerOnePositionX = ?,
                playerOnePositionY = ?,
                playerTwoPositionX = ?,
                playerTwoPositionY = ?,
                ballPositionX = ?,
                ballPositionY = ?,
                elapsedTime = ?,
                createdAt = ?
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(db_url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, session.getPlayerOnePoint());
            ps.setInt(2, session.getPlayerTwoPoint());
            ps.setDouble(3, session.getPlayerOnePosition().x);
            ps.setDouble(4, session.getPlayerOnePosition().y);
            ps.setDouble(5, session.getPlayerTwoPosition().x);
            ps.setDouble(6, session.getPlayerTwoPosition().y);
            ps.setDouble(7, session.getBallPosition().x);
            ps.setDouble(8, session.getBallPosition().y);
            ps.setBigDecimal(9, new java.math.BigDecimal(session.getElapsedTime()));
            ps.setTimestamp(10, session.getCreatedAt());
            ps.setInt(11, session.getId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
