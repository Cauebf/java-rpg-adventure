package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];

        getTileImage();
        loadMap("map01.txt");
    }

    public void getTileImage() {

        try {

            tile[0] = new Tile();
            tile[0].image = loadTileImage("grass01.png");

            tile[1] = new Tile();
            tile[1].image = loadTileImage("water00.png");

            tile[2] = new Tile();
            tile[2].image = loadTileImage("wall.png");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading tile images.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private BufferedImage loadTileImage(String fileName) throws IOException {
        return ImageIO.read(getClass().getClassLoader().getResourceAsStream("res/tiles/" + fileName));
    }

    public void loadMap(String fileName) {

        try {

            InputStream is = getClass().getClassLoader().getResourceAsStream("res/maps/" + fileName); // Load the map
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Read the map

            int col = 0;
            int row = 0;

            // Read the map and store it in the mapTileNum array
            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

                String line = br.readLine(); // Read a line from the map as a string

                while (col < gp.maxScreenCol) {

                    String numbers[] = line.split(" "); // Split the line into an array of strings
                    int num = Integer.parseInt(numbers[col]); // Convert the string to an integer

                    mapTileNum[row][col] = num; // Store the integer in the mapTileNum array
                    col++;
                }

                if (col == gp.maxScreenCol) {
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

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        // Draw the map tile by tile
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[row][col];

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
