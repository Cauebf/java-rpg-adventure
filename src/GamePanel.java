import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    
    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576px

    // FPS
    final int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread; // The thread that runs the game

    // Player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // To avoid flickering
        this.addKeyListener(keyH); // To capture keyboard input
        this.setFocusable(true); 
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start(); // starts the thread and calls the run method
    }

    @Override
    public void run() {
        // Game loop

        double drawInterval = 1000000000 / FPS; // time between each frame: 0.01666 seconds (60 FPS)
        double delta = 0; // Accumulates the time passed between frames
        long lastTime = System.nanoTime(); 
        long currentTime;

        while(gameThread != null) {    
            currentTime = System.nanoTime(); 
            delta += (currentTime - lastTime) / drawInterval; // add the time passed since the last loop to delta
            lastTime = currentTime;

            // when delta reaches 1, enough time has passed for the next frame
            if (delta >= 1) { 
                update(); // update information such as character positions
                repaint(); // draw the screen with the updated information (call paintComponent method)
                delta--; 
            }
        }
    }

    public void update() {

        if (keyH.upPressed) {
            playerY -= playerSpeed;
        }
        if (keyH.downPressed) {
            playerY += playerSpeed;
        }
        if (keyH.leftPressed) {
            playerX -= playerSpeed;
        }
        if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
    }

    @Override // JPanel method to paint the screen
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g; // Cast the Graphics object to Graphics2D (more features)

        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize); // draw a rectangle
        g2.dispose(); // good practice to save memory
    }
}