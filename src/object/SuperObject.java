package object;

import jZelda.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    // Frame per l'animazione degli oggetti (es. torce)
    public BufferedImage frame1, frame2, frame3;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean animated = false; // true solo per gli oggetti che si animano
    public int drawSize; // dimensione di disegno in pixel (0 = usa tileSize di default)

    public boolean interactable = false; // NUOVO: true solo per gli oggetti che rispondono al tasto E
    public int widthInTiles = 1;   // quante celle copre in larghezza (default 1)
    public int heightInTiles = 1;  // quante celle copre in altezza (default 1)
    public int value = 0; // valore dell'oggetto
    public boolean visible = true;

    // Fa avanzare l'animazione ciclando tra i 3 frame
    public void update() {
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 3;
            else if (spriteNum == 3) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    // NUOVO: metodo di interazione, di base non fa nulla.
    // Ogni oggetto interagibile lo ridefinisce a modo suo (override).
    public void interact() {
        // vuoto di default
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        if (!visible) return;
        // Se l'oggetto non ha immagine (es. portale invisibile), non disegna nulla
        if (image == null && !animated) {
            return;
        }
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        int size = (drawSize > 0) ? drawSize : gp.tileSize;

        // Culling: disegna solo se l'oggetto ricade dentro lo schermo
        if (screenX + size > 0 &&
                screenX < gp.screenWidth &&
                screenY + size > 0 &&
                screenY < gp.screenHeight) {
            // Se l'oggetto è animato sceglie il frame corrente, altrimenti usa l'immagine fissa
            BufferedImage img = image;
            if (animated && frame1 != null) {
                if (spriteNum == 1) img = frame1;
                if (spriteNum == 2) img = frame2;
                if (spriteNum == 3) img = frame3;
            }

            g2.drawImage(img, screenX, screenY, size, size, null);
        }
    }
}
