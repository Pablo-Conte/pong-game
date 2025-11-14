import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.*;

import database.GameDAO;
import database.init.DatabaseInit;
import database.model.GameSession;
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
        GameDAO gameDAO = new GameDAO(props);

        GameSession gameSession = gameDAO.initOrReloadGame();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Pong-Game");
        System.out.println(gameSession);
        GamePanel pa = new GamePanel(gameSession);
        getContentPane().add(pa, BorderLayout.CENTER);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameDAO.updateGameSession(gameSession);

                dispose();
            }

        });
    }
}
