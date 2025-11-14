import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;

import database.init.DatabaseInit;
import gamerunner.GamePanel;

public class Main extends JFrame {
    public static void main(String[] args) {
        Main wpe = new Main();
        wpe.setup();
    }

    public static Properties initializeProps() {
        Properties props = new Properties();

        try (InputStream input = Main.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties não encontrado");
            }

            props.load(input);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String host = props.getProperty("db.host");
        System.out.println("Host: " + host);

        return props;
    }

    public void setup() {
        Properties props = initializeProps();
        new DatabaseInit(props);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Pong-Game");
        GamePanel pa = new GamePanel();
        getContentPane().add(pa, BorderLayout.CENTER);
        setVisible(true);
    }
}
