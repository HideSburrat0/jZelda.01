package model;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ENM_Bat extends Enemy {

    // Il pipistrello ha 6 frame frontali
    public BufferedImage[] frames = new BufferedImage[6];

    public ENM_Bat(GamePanel gp) {
        super(gp);

        name = "Bat";
        speed = 3;
        life = 1;
        scoreValue = 1;
        direction = "down";
        enemySize = gp.tileSize + 16;
        dropChance = 33;
        dropValue = 1;

        getBatImage();
    }

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

    // Animazione a 6 frame
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

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 6) spriteNum = 1;
            spriteCounter = 0;
        }
    }
}