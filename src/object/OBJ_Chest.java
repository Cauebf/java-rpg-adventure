package object;

import main.GamePanel;

public class OBJ_Chest extends SuperObject {

    public OBJ_Chest(GamePanel gp) {

        name = "Chest";
        setupImage("chest.png", gp);
    }
}
