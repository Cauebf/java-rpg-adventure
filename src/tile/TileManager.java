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

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("world01.txt");
    }

    public void getTileImage() {

        setup(0, "grass01.png", false);
        setup(1, "wall.png", true);
        setup(2, "water01.png", true);
        setup(3, "earth.png", false);
        setup(4, "tree.png", true);
        setup(5, "road00.png", false);
    }

    public void setup(int index, String imagePath, boolean collision) {

        UtilityTool tool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/tiles/" + imagePath)); // Load the tile image
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
