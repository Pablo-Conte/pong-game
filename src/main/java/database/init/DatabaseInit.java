package database.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseInit {

    private static final String ADMIN_URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/pong";

    private static final String TARGET_DB = "pong";

    public DatabaseInit(Properties props) {
        String USER = props.getProperty("db.user");
        String PASS = props.getProperty("db.pass");

        try {
            checkAndCreateDatabase(USER, PASS);
            createTablesIfNotExists(USER, PASS);
            System.out.println("Banco verificado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkAndCreateDatabase(String user, String pass) throws SQLException {

        try (Connection conn = DriverManager.getConnection(ADMIN_URL, user, pass)) {
            String sql = "SELECT 1 FROM pg_database WHERE datname = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, TARGET_DB);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Banco não existe. Criando...");
                    try (Statement st = conn.createStatement()) {
                        st.executeUpdate("CREATE DATABASE " + TARGET_DB);
                    }
                    System.out.println("Banco criado!");
                } else {
                    System.out.println("Banco já existe.");
                }
            }
        }
    }

    private static void createTablesIfNotExists(String user, String pass) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, user, pass)) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS GameSession (
                            id SERIAL PRIMARY KEY,
                            playerOnePoint int DEFAULT(0),
                            playerTwoPoint int DEFAULT(0),
                            playerOnePosition POINT,
                            playerTwoPosition POINT,
                            ballPosition POINT,
                            elapsedMillis BIGINT DEFAULT(0),
                            createdAt DATE DEFAULT CURRENT_DATE
                        );
                    """;

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
    }
}
