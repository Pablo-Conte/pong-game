package database.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseInit {

    private static final String TARGET_DB = "pong";

    public DatabaseInit(Properties props) {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        String db_admin_url = props.getProperty("db.admin.url");
        String db_url = props.getProperty("db.url");

        try {
            checkAndCreateDatabase(user, pass, db_admin_url);
            createTablesIfNotExists(user, pass, db_url);
            System.out.println("Banco verificado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkAndCreateDatabase(String user, String pass, String db_admin_url) throws SQLException {

        try (Connection conn = DriverManager.getConnection(db_admin_url, user, pass)) {
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

    private static void createTablesIfNotExists(String user, String pass, String db_url) throws SQLException {
        try (Connection conn = DriverManager.getConnection(db_url, user, pass)) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS GameSession (
                            id SERIAL PRIMARY KEY,
                            playerOnePoint int DEFAULT(0),
                            playerTwoPoint int DEFAULT(0),
                            playerOnePosition POINT DEFAULT(POINT(350,10)),
                            playerTwoPosition POINT DEFAULT(POINT(350,541)),
                            ballPosition POINT,
                            elapsedTime BIGINT DEFAULT(0),
                            createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );
                    """;

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
    }
}
