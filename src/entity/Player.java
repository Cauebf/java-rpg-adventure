package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public final int screenX, screenY; // Player position on the screen
    int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        // Center the player on the screen
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Set the solid area of the player
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

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

            // Check which direction the player is moving
            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            // Check tile collision
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            // Check object collision
            int objectIndex = gp.collisionChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            // If collision is false, player is free to move
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
    }

    public void pickUpObject(int i) {

        // 999 means that no object was found
        if (i != 999) {

            String objectName = gp.object[i].name;

            switch (objectName) {
                case "Key":

                    // Increase the number of keys and remove the key from the map
                    gp.playSE(1);
                    hasKey++;
                    gp.object[i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door":

                    if (hasKey > 0) {
                        // Decrease the number of keys and remove the door from the map
                        gp.playSE(3);
                        gp.object[i] = null;
                        hasKey--;
                    }
                    break;
                case "Boots":

                    // Increase the player's speed and remove the boots from the map
                    gp.playSE(2);
                    speed += 2;
                    gp.object[i] = null;
                    break;
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
