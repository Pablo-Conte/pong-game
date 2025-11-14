package main.java.database.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {

    private static final String ADMIN_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/meu_jogo";
    private static final String USER = "postgres";
    private static final String PASS = "senha";

    private static final String TARGET_DB = "meu_jogo";

    public static void main(String[] args) {
        try {
            checkAndCreateDatabase();
            createTablesIfNotExists();
            System.out.println("Banco verificado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkAndCreateDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(ADMIN_URL, USER, PASS)) {
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

    private static void createTablesIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS jogadores (
                            id SERIAL PRIMARY KEY,
                            nome VARCHAR(100),
                            nivel INT DEFAULT 1,
                            experiencia INT DEFAULT 0
                        );
                    """;

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
    }
}
