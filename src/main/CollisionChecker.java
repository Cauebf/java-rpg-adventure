package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        // Get the edges of the entity's collision area in the world
        int leftWorldX = entity.worldX + entity.solidArea.x;
        int rightWorldX = leftWorldX + entity.solidArea.width;
        int topWorldY = entity.worldY + entity.solidArea.y;
        int bottomWorldY = topWorldY + entity.solidArea.height;

        // Convert the collision area position from pixels to tile coordinates
        int leftCol = leftWorldX / gp.tileSize;
        int rightCol = rightWorldX / gp.tileSize;
        int topRow = topWorldY / gp.tileSize;
        int bottomRow = bottomWorldY / gp.tileSize;

        int tileNum1 = 0;
        int tileNum2 = 0;

        // Check the tiles the entity would reach based on its direction
        switch (entity.direction) {
            case "up":
                topRow = (topWorldY - entity.speed) / gp.tileSize;

                // Top-left and top-right tiles
                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol];
                tileNum2 = gp.tileM.mapTileNum[topRow][rightCol];
                break;

            case "down":
                bottomRow = (bottomWorldY + entity.speed) / gp.tileSize;

                // Bottom-left and bottom-right tiles
                tileNum1 = gp.tileM.mapTileNum[bottomRow][leftCol];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol];
                break;

            case "left":
                leftCol = (leftWorldX - entity.speed) / gp.tileSize;

                // Top-left and bottom-left tiles
                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][leftCol];
                break;

            case "right":
                rightCol = (rightWorldX + entity.speed) / gp.tileSize;

                // Top-right and bottom-right tiles
                tileNum1 = gp.tileM.mapTileNum[topRow][rightCol];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol];
                break;
        }

        // Check if either tile has collision
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        // Check all objects in the game
        for (int i = 0; i < gp.object.length; i++) {

            if (gp.object[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                gp.object[i].solidArea.x = gp.object[i].worldX + gp.object[i].solidArea.x;
                gp.object[i].solidArea.y = gp.object[i].worldY + gp.object[i].solidArea.y;

                // Move the entity's solid area in the direction the entity is moving
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // Check if the entity will collide with the object in the direction it is moving
                if (entity.solidArea.intersects(gp.object[i].solidArea)) {

                    // Stop the entity if the object has collision
                    if (gp.object[i].collision) {
                        entity.collisionOn = true;
                    }

                    // Return the object's index if the entity is the player
                    if (player) {
                        index = i;
                    }
                }

                // Reset the collision areas to their original positions
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.object[i].solidArea.x = gp.object[i].solidAreaDefaultX;
                gp.object[i].solidArea.y = gp.object[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    // NPC or Monster collision
    public int checkEntity(Entity entity, Entity[] target) {

        // If collision doesn't happen, return 999, otherwise return the target's index
        int index = 999;

        // Check through the target array
        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the target's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                // Move the entity's solid area in the direction the entity is moving
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // Check if the entity will collide with the target in the direction it is moving
                if (entity.solidArea.intersects(target[i].solidArea)) {

                    entity.collisionOn = true;
                    index = i;
                }

                // Reset the collision areas to their original positions
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public void checkPlayer(Entity entity) {

        // Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the player's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Move the entity's solid area in the direction the entity is moving
        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;

            case "down":
                entity.solidArea.y += entity.speed;
                break;

            case "left":
                entity.solidArea.x -= entity.speed;
                break;

            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        // Check if the entity will collide with the player in the direction it is moving
        if (entity.solidArea.intersects(gp.player.solidArea)) {

            entity.collisionOn = true;
        }

        // Reset the collision areas to their original positions
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }
}
