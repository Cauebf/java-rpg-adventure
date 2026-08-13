package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    KeyHandler keyH;
    public final int screenX, screenY; // Player position on the screen
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH = keyH;

        // Center the player on the screen
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Set the solid area of the player
        solidArea = new Rectangle(12, 20, 24, 28);
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

        up1 = setup("/player/player_up_1.png");
        up2 = setup("/player/player_up_2.png");
        down1 = setup("/player/player_down_1.png");
        down2 = setup("/player/player_down_2.png");
        left1 = setup("/player/player_left_1.png");
        left2 = setup("/player/player_left_2.png");
        right1 = setup("/player/player_right_1.png");
        right2 = setup("/player/player_right_2.png");
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

            // Check NPC collision
            int npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

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
        } else {

            // Idle animation

            // Switch sprite every 20 frames when not moving
            standCounter++;
            if (standCounter > 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {

        // 999 means that no object was found
        if (i != 999) {

        }
    }

    public void interactNPC(int i) {

        // 999 means that no NPC was found
        if (i != 999) {

            // If the player presses enter, the NPC will speak
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }

        gp.keyH.enterPressed = false;
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
        g2.drawImage(image, screenX, screenY, null);
    }
}
