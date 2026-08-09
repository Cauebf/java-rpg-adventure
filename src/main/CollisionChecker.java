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
}
