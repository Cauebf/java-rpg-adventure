package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_Key extends SuperObject {

    public OBJ_Key() {

        name = "Key";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/key.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading " + name + " object image.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
