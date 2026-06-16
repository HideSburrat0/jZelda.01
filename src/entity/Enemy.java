package entity;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity {

    GamePanel gp;
    public int life; // punti vita del nemico
    public int damage = 1;
    public int scoreValue = 1;
    public int enemySize; // dimensione di disegno (default = una casella)
    public boolean alive = true;
    public int dropChance = 33;  // probabilità (%) di lasciare una rupia
    public int dropValue = 1;    // quante rupie vale il drop

    public Enemy(GamePanel gp) {
        this.gp = gp;
        enemySize = gp.tileSize;
        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 20;
        solidArea.width = 24;
        solidArea.height = 24;
    }

    // Carica gli sprite del nemico dai 3 file (su, giù, lato). I 4 frame sono affiancati 16x16.
    // I file di "lato" servono per destra; la sinistra verrà specchiata nel disegno.
    public void getEnemyImage(String upFile, String downFile, String sideFile) {
        try {
            BufferedImage up = ImageIO.read(getClass().getResourceAsStream(upFile));
            BufferedImage down = ImageIO.read(getClass().getResourceAsStream(downFile));
            BufferedImage side = ImageIO.read(getClass().getResourceAsStream(sideFile));

            // Ritaglia i 4 frame affiancati da ogni sheet (16x16)
            up1 = up.getSubimage(0, 0, 16, 16);
            up2 = up.getSubimage(16, 0, 16, 16);
            up3 = up.getSubimage(32, 0, 16, 16);
            up4 = up.getSubimage(48, 0, 16, 16);

            down1 = down.getSubimage(0, 0, 16, 16);
            down2 = down.getSubimage(16, 0, 16, 16);
            down3 = down.getSubimage(32, 0, 16, 16);
            down4 = down.getSubimage(48, 0, 16, 16);

            // I frame laterali li usiamo sia per destra che (specchiati) per sinistra
            right1 = side.getSubimage(0, 0, 16, 16);
            right2 = side.getSubimage(16, 0, 16, 16);
            right3 = side.getSubimage(32, 0, 16, 16);
            right4 = side.getSubimage(48, 0, 16, 16);

            left1 = right1;
            left2 = right2;
            left3 = right3;
            left4 = right4;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Decide la prossima mossa del nemico. Vuoto di default: ogni tipo lo ridefinisce.
    public void setAction() {
        // comportamento specifico nelle sottoclassi
    }

    public void update() {
        setAction(); // ogni nemico decide cosa fare

        // Controlla le collisioni col mondo prima di muoversi
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // Si muove solo se non c'è collisione
        if (collisionOn == false) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        // Anima i 4 frame della camminata (come il player)
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 3;
            else if (spriteNum == 3) spriteNum = 4;
            else if (spriteNum == 4) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        // Posizione del nemico sullo schermo (rispetto alla camera del player)
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Culling: disegna solo se il nemico è dentro lo schermo
        if (screenX + gp.tileSize > 0 && screenX < gp.screenWidth &&
                screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {

            BufferedImage image = null;
            boolean flip = false; // se true, specchia l'immagine (per la sinistra)

            switch (direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    if (spriteNum == 3) image = up3;
                    if (spriteNum == 4) image = up4;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    if (spriteNum == 3) image = down3;
                    if (spriteNum == 4) image = down4;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    if (spriteNum == 3) image = right3;
                    if (spriteNum == 4) image = right4;
                    break;
                case "left":
                    // Usa i frame di destra ma specchiati
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    if (spriteNum == 3) image = left3;
                    if (spriteNum == 4) image = left4;
                    flip = true;
                    break;
            }

            if (flip) {
                g2.drawImage(image, screenX + enemySize, screenY, -enemySize, enemySize, null);
            } else {
                g2.drawImage(image, screenX, screenY, enemySize, enemySize, null);
            }
        }
    }
}