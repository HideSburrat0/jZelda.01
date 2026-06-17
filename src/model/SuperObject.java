package model;

import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    // Frame per l'animazione (es. torce)
    public BufferedImage frame1, frame2, frame3;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean animated = false;
    public int drawSize; // dimensione di disegno in pixel (0 = usa tileSize)

    public boolean interactable = false;
    public int widthInTiles = 1;
    public int heightInTiles = 1;
    public int value = 0;
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

    // Metodo di interazione, di base non fa nulla (override nelle sottoclassi)
    public void interact() {
    }
}