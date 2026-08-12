package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("world01.txt");
    }

    public void getTileImage() {

         // PLACEHOLDER
        int indexes[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for(int i = 0; i < indexes.length; i++){
            setup(indexes[i], "grass00", false);
        }
        
        // GRASS
        setup(10, "grass00", false);
        setup(11, "grass01", false);

        // WATER
        indexes = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        for(int i = 0; i < indexes.length; i++){
            String waterIndex = "water" + String.format("%02d", i);
            setup(indexes[i], waterIndex, true);
        }

        // ROAD
        indexes = new int[]{26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
        for(int i = 0; i < indexes.length; i++){
            String roadIndex = "road" + String.format("%02d", i);
            setup(indexes[i], roadIndex, false);
        }

        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41,"tree", true);
    }

    public void setup(int index, String imagePath, boolean collision) {

        UtilityTool tool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/tiles/" + imagePath + ".png")); // Load the tile image
            tile[index].image = tool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize); // Scale the tile image (improves performance instead of scaling during the game loop)
            tile[index].collision = collision;
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading tile image: " + imagePath);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadMap(String fileName) {

        try {

            InputStream is = getClass().getClassLoader().getResourceAsStream("res/maps/" + fileName); // Load the map
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Read the map

            int col = 0;
            int row = 0;

            // Read the map from the file and store it in the mapTileNum array
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine(); // Read a line from the map as a string

                while (col < gp.maxWorldCol) {

                    String numbers[] = line.split(" "); // Split the line into an array of strings
                    int num = Integer.parseInt(numbers[col]); // Convert the string to an integer

                    mapTileNum[row][col] = num; // Store the integer in the mapTileNum array
                    col++;
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close(); // Close the BufferedReader
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading map.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            // Get the tile number stored at this position
            int tileNum = mapTileNum[worldRow][worldCol];

            // Calculate the tile position in the world
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // Convert the world position to a screen position relative to the player (camera effect)
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Check if the tile is inside the player's visible area
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                // Draw the tile on the screen
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
