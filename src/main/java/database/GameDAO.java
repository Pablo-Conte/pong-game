package database;

import database.model.GameSession;
import org.postgresql.geometric.PGpoint;

import java.math.BigInteger;
import java.sql.*;
import java.util.Properties;

public class GameDAO {;
    private static final String TARGET_DB = "pong";
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

                PGpoint p1Pos = (PGpoint) rs.getObject("playerOnePosition");
                PGpoint p2Pos = (PGpoint) rs.getObject("playerTwoPosition");
                PGpoint ballPos = (PGpoint) rs.getObject("ballPosition");

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

        String sql = """
            INSERT INTO GameSession DEFAULT VALUES
            RETURNING id, elapsedTime, createdat;
        """;

        try (Connection conn = DriverManager.getConnection(db_url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                BigInteger elapsedTime = rs.getBigDecimal("elapsedTime").toBigInteger();
                Timestamp createdAt = rs.getTimestamp("createdat");
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
            return null;

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
                playerOnePosition = ?,
                playerTwoPosition = ?,
                ballPosition = ?,
                elapsedTime = ?,
                createdAt = ?
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(db_url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, session.getPlayerOnePoint());
            ps.setInt(2, session.getPlayerTwoPoint());
            ps.setObject(3, session.getPlayerOnePosition());
            ps.setObject(4, session.getPlayerTwoPosition());
            ps.setObject(5, session.getBallPosition());
            ps.setBigDecimal(6, new java.math.BigDecimal(session.getElapsedTime()));
            ps.setTimestamp(7, session.getCreatedAt());
            ps.setInt(8, session.getId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
