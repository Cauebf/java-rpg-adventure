package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX, screenY; // Player position on the screen

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        // Center the player on the screen
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try {
            up1 = loadImage("player_up_1.png");
            up2 = loadImage("player_up_2.png");
            down1 = loadImage("player_down_1.png");
            down2 = loadImage("player_down_2.png");
            left1 = loadImage("player_left_1.png");
            left2 = loadImage("player_left_2.png");
            right1 = loadImage("player_right_1.png");
            right2 = loadImage("player_right_2.png");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading player images.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private BufferedImage loadImage(String fileName) throws IOException {
        return ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/player/" + fileName));
    }

    public void update() {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            
            // Player movement
            if (keyH.upPressed) {
                direction = "up";
                worldY -= speed;
            }
            if (keyH.downPressed) {
                direction = "down";
                worldY += speed;
            }
            if (keyH.leftPressed) {
                direction = "left";
                worldX -= speed;
            }
            if (keyH.rightPressed) {
                direction = "right";
                worldX += speed;
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
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

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

        // Draw the player
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
