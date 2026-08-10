package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    protected void loadImage(String fileName) {

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/objects/" + fileName));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading " + name + " object image.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        // Get the object's position on the screen relative to the player
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the object is inside the player's visible area
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            // Draw the object on the screen
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
