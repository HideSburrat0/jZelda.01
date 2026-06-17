package model;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity {

    GamePanel gp;
    public int life;
    public int damage = 1;
    public int scoreValue = 1;
    public int enemySize;
    public boolean alive = true;
    public int dropChance = 33;
    public int dropValue = 1;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        enemySize = gp.tileSize;
        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 20;
        solidArea.width = 24;
        solidArea.height = 24;
    }

    // Carica gli sprite del nemico dai 3 file (su, giù, lato). 4 frame affiancati 16x16.
    public void getEnemyImage(String upFile, String downFile, String sideFile) {
        try {
            BufferedImage up = ImageIO.read(getClass().getResourceAsStream(upFile));
            BufferedImage down = ImageIO.read(getClass().getResourceAsStream(downFile));
            BufferedImage side = ImageIO.read(getClass().getResourceAsStream(sideFile));

            up1 = up.getSubimage(0, 0, 16, 16);
            up2 = up.getSubimage(16, 0, 16, 16);
            up3 = up.getSubimage(32, 0, 16, 16);
            up4 = up.getSubimage(48, 0, 16, 16);

            down1 = down.getSubimage(0, 0, 16, 16);
            down2 = down.getSubimage(16, 0, 16, 16);
            down3 = down.getSubimage(32, 0, 16, 16);
            down4 = down.getSubimage(48, 0, 16, 16);

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

    // Decide la prossima mossa. Vuoto di default: ogni tipo lo ridefinisce.
    public void setAction() {
    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (collisionOn == false) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 3;
            else if (spriteNum == 3) spriteNum = 4;
            else if (spriteNum == 4) spriteNum = 1;
            spriteCounter = 0;
        }
    }
}