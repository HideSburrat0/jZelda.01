package model;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shop extends SuperObject {

    GamePanel gp;

    public OBJ_Shop(GamePanel gp) {
        this.gp = gp;
        name = "Shop";
        interactable = true;

        try {
            // La bancarella è 96x64 nello sheet, a partire da (160, 96)
            image = ImageIO.read(getClass().getResourceAsStream("/objects/2_indoors.png")).getSubimage(160, 96, 96, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        gp.model.gameState = gp.model.shopState;
        gp.model.shopSelection = 0;
    }
}