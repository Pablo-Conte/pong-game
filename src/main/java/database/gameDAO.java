package database;

public class gameDAO {
    private static final String URL = "jdbc:postgresql://localhost/gabriel";
    private static final String USER = "gabriel";
    private static final String PASSWORD = "12345";

    public void verifyGameState(String name, String email) {
        // just example
        // String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        // try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        // PreparedStatement stmt = conn.prepareStatement(sql)) {

        // stmt.setString(1, name);
        // stmt.setString(2, email);
        // stmt.executeUpdate();
        // System.out.println("User created successfully!");

        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        // }
        // String sql = "SELECT id, name, email FROM users";
        // List<String> users = new ArrayList<>();

        // try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        // Statement stmt = conn.createStatement();
        // ResultSet rs = stmt.executeQuery(sql)) {

        // while (rs.next()) {
        // users.add(
        // rs.getInt("id") + " - " +
        // rs.getString("name") + " (" +
        // rs.getString("email") + ")"
        // );
        // }

        // } catch (SQLException e) {
        // e.printStackTrace();
        // }

        // return users;
    }
}
