import java.awt.BorderLayout;
import javax.swing.JFrame;

public class WindowPaintEvents extends JFrame{
    GamePanel pa;

    public static void main(String[] args) {
        WindowPaintEvents wpe = new WindowPaintEvents();
        wpe.setup();
    }
    
    public void setup(){
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setTitle("Main Window");
        pa = new GamePanel();
        getContentPane().add(pa, BorderLayout.CENTER);
        setVisible(true);
    }
}
