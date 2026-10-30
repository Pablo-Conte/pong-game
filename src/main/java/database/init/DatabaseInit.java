package database.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseInit {

    public DatabaseInit(Properties props) {
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        String db_url = props.getProperty("db.url");

        try {
            createTablesIfNotExists(user, pass, db_url);
            System.out.println("Banco verificado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExists(String user, String pass, String db_url) throws SQLException {
        try (Connection conn = DriverManager.getConnection(db_url, user, pass)) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS GameSession (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            playerOnePoint INT DEFAULT 0,
                            playerTwoPoint INT DEFAULT 0,
                            playerOnePositionX DOUBLE DEFAULT 350,
                            playerOnePositionY DOUBLE DEFAULT 10,
                            playerTwoPositionX DOUBLE DEFAULT 350,
                            playerTwoPositionY DOUBLE DEFAULT 541,
                            ballPositionX DOUBLE DEFAULT 380,
                            ballPositionY DOUBLE DEFAULT 90,
                            elapsedTime BIGINT DEFAULT 0,
                            createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );
                    """;

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
    }
}
