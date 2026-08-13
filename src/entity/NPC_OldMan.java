package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 1;

        getImage();
    }

    public void getImage() {

        up1 = setup("/npc/oldman_up_1.png");
        up2 = setup("/npc/oldman_up_2.png");
        down1 = setup("/npc/oldman_down_1.png");
        down2 = setup("/npc/oldman_down_2.png");
        left1 = setup("/npc/oldman_left_1.png");
        left2 = setup("/npc/oldman_left_2.png");
        right1 = setup("/npc/oldman_right_1.png");
        right2 = setup("/npc/oldman_right_2.png");
    }

    @Override
    public void setAction() {

        actionLockCounter++;

        // NPC will change direction every 120 frames (2 seconds)
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // 1-100

            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
}
