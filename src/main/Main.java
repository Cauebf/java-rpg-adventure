package main;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) throws Exception {

        System.setProperty("sun.java2d.opengl", "true"); // Enable OpenGL support

        JFrame window = new JFrame(); // Create a window object
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window when clicked on the X
        window.setResizable(false);
        window.setTitle("The Legend of Mai'qou");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // Adjust the size of the window

        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}
