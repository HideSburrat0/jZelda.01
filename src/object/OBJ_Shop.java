package object;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
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

    // Disegno personalizzato: la bancarella è un rettangolo (non quadrato), quindi
    // la disegniamo con larghezza e altezza proporzionate per non deformarla
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Disegna a dimensione raddoppiata mantenendo le proporzioni (96x64 → 192x128)
        int drawW = 96 * 3; // larghezza disegnata
        int drawH = 64 * 3; // altezza disegnata

        // Culling
        if (screenX + drawW > 0 && screenX < gp.screenWidth &&
                screenY + drawH > 0 && screenY < gp.screenHeight) {
            g2.drawImage(image, screenX, screenY, drawW, drawH, null);
        }
    }
}