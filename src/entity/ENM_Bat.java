package entity;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ENM_Bat extends Enemy {

    // Il pipistrello ha 6 frame frontali (non ha direzioni diverse)
    BufferedImage[] frames = new BufferedImage[6];

    public ENM_Bat(GamePanel gp) {
        super(gp);

        name = "Bat";
        speed = 3;
        life = 1;
        scoreValue = 1;
        direction = "down";
        enemySize = gp.tileSize + 16; // dimensione (regola tu)
        dropChance = 33;
        dropValue = 1;

        getBatImage();
    }

    // Carica i 6 frame affiancati dallo sheet del pipistrello
    public void getBatImage() {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/enemy/bat.png"));
            for (int i = 0; i < 6; i++) {
                frames[i] = sheet.getSubimage(i * 16, 0, 16, 16);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Comportamento: insegue il player
    @Override
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 30) {
            int diffX = gp.player.worldX - worldX;
            int diffY = gp.player.worldY - worldY;

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX < 0) direction = "left";
                else direction = "right";
            } else {
                if (diffY < 0) direction = "up";
                else direction = "down";
            }
            actionLockCounter = 0;
        }
    }

    // Animazione a 6 frame (sovrascrive update di Enemy)
    @Override
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

        // Anima i 6 frame (invece dei 4 standard)
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 6) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    // Il pipistrello si disegna con i suoi 6 frame (sovrascrive draw di Enemy)
    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Culling: disegna solo se è dentro lo schermo
        if (screenX + enemySize > 0 && screenX < gp.screenWidth &&
                screenY + enemySize > 0 && screenY < gp.screenHeight) {

            // Mostra il frame corrente (spriteNum va da 1 a 6)
            BufferedImage image = frames[spriteNum - 1];
            g2.drawImage(image, screenX, screenY, enemySize, enemySize, null);
        }
    }
}