package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    GamePanel gp;
    public int worldX, worldY; // Position on the map
    public int speed;

    // Entity image
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    // Animation
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public int actionLockCounter = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {

    }

    public void update() {

        setAction();

        // Check collision
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkObject(this, false);
        gp.collisionChecker.checkPlayer(this);

        // If collision is false, the entity is free to move
        if (!collisionOn) {
            if (direction == "up") {
                worldY -= speed;
            }
            if (direction == "down") {
                worldY += speed;
            }
            if (direction == "left") {
                worldX -= speed;
            }
            if (direction == "right") {
                worldX += speed;
            }
        }

        // Sprite Animation: switch sprite every 12 frames when moving
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        // Get the entity's position on the screen relative to the player
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the entity is inside the player's visible area
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            // Select the image based on the entity's direction
            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            // Draw the entity on the screen
            g2.drawImage(image, screenX, screenY, null);
        }
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool tool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res" + imagePath));
            image = tool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the entity image (improves performance instead of scaling during the game loop)
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading entity image: " + imagePath);
            e.printStackTrace();
            System.exit(1);
        }

        return image;
    }
}
