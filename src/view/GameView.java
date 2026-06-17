package view;

import jZelda.GamePanel;
import model.ENM_Bat;
import model.Enemy;
import model.OBJ_Shop;
import model.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;

// VIEW: disegna lo stato del gioco. Non modifica i dati, non legge input.
public class GameView {

    GamePanel gp;

    public GameView(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {

        // 1. La mappa
        drawMap(g2);

        // 2. Gli oggetti
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                drawObject(g2, gp.obj[i]);
            }
        }

        // 3. I nemici
        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] != null) {
                drawEnemy(g2, gp.enemy[i]);
            }
        }

        // 4. Il player
        drawPlayer(g2);

        // 5. L'interfaccia
        gp.ui.draw(g2);
    }

    // Disegna i tile della mappa (leggendo i dati dal TileManager)
    public void drawMap(Graphics2D g2) {
        var tileM = gp.tileM;

        for (int layer = 0; layer < 3; layer++) {
            int worldCol = 0;
            int worldRow = 0;
            while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
                int tileNum = tileM.mapTileNum[layer][worldCol][worldRow];
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                if (screenX + gp.tileSize > 0 &&
                        screenX < gp.screenWidth &&
                        screenY + gp.tileSize > 0 &&
                        screenY < gp.screenHeight) {

                    if (tileNum > 0 && tileNum < tileM.tile.length && tileM.tile[tileNum] != null) {
                        g2.drawImage(tileM.tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }

                worldCol++;
                if (worldCol == gp.maxWorldCol) {
                    worldCol = 0;
                    worldRow++;
                }
            }
        }
    }

    // Disegna un oggetto leggendo i suoi dati
    public void drawObject(Graphics2D g2, SuperObject obj) {
        if (!obj.visible) return;

        int screenX = obj.worldX - gp.player.worldX + gp.player.screenX;
        int screenY = obj.worldY - gp.player.worldY + gp.player.screenY;

        // Caso speciale: lo shop ha proporzioni 96x64 (rettangolo), disegnato x3
        if (obj instanceof OBJ_Shop) {
            int drawW = 96 * 3;
            int drawH = 64 * 3;
            if (screenX + drawW > 0 && screenX < gp.screenWidth &&
                    screenY + drawH > 0 && screenY < gp.screenHeight) {
                g2.drawImage(obj.image, screenX, screenY, drawW, drawH, null);
            }
            return;
        }

        // Caso standard: immagine (o frame animato) + dimensione quadrata
        if (obj.image == null && !obj.animated) {
            return; // oggetto invisibile (es. portale senza sprite)
        }

        int size = (obj.drawSize > 0) ? obj.drawSize : gp.tileSize;

        if (screenX + size > 0 && screenX < gp.screenWidth &&
                screenY + size > 0 && screenY < gp.screenHeight) {

            // Se animato a 3 frame (torce), sceglie il frame corrente
            BufferedImage img = obj.image;
            if (obj.animated && obj.frame1 != null) {
                if (obj.spriteNum == 1) img = obj.frame1;
                if (obj.spriteNum == 2) img = obj.frame2;
                if (obj.spriteNum == 3) img = obj.frame3;
            }

            g2.drawImage(img, screenX, screenY, size, size, null);
        }
    }

    // Disegna un nemico leggendo i suoi dati
    public void drawEnemy(Graphics2D g2, Enemy e) {
        int screenX = e.worldX - gp.player.worldX + gp.player.screenX;
        int screenY = e.worldY - gp.player.worldY + gp.player.screenY;

        if (!(screenX + e.enemySize > 0 && screenX < gp.screenWidth &&
                screenY + e.enemySize > 0 && screenY < gp.screenHeight)) {
            return;
        }

        // Il pipistrello ha i suoi 6 frame frontali
        if (e instanceof ENM_Bat) {
            ENM_Bat bat = (ENM_Bat) e;
            BufferedImage image = bat.frames[e.spriteNum - 1];
            g2.drawImage(image, screenX, screenY, e.enemySize, e.enemySize, null);
            return;
        }

        // Altri nemici: 4 frame per direzione, flip per sinistra
        BufferedImage image = null;
        boolean flip = false;

        switch (e.direction) {
            case "up":
                if (e.spriteNum == 1) image = e.up1;
                if (e.spriteNum == 2) image = e.up2;
                if (e.spriteNum == 3) image = e.up3;
                if (e.spriteNum == 4) image = e.up4;
                break;
            case "down":
                if (e.spriteNum == 1) image = e.down1;
                if (e.spriteNum == 2) image = e.down2;
                if (e.spriteNum == 3) image = e.down3;
                if (e.spriteNum == 4) image = e.down4;
                break;
            case "right":
                if (e.spriteNum == 1) image = e.right1;
                if (e.spriteNum == 2) image = e.right2;
                if (e.spriteNum == 3) image = e.right3;
                if (e.spriteNum == 4) image = e.right4;
                break;
            case "left":
                if (e.spriteNum == 1) image = e.left1;
                if (e.spriteNum == 2) image = e.left2;
                if (e.spriteNum == 3) image = e.left3;
                if (e.spriteNum == 4) image = e.left4;
                flip = true;
                break;
        }

        if (flip) {
            g2.drawImage(image, screenX + e.enemySize, screenY, -e.enemySize, e.enemySize, null);
        } else {
            g2.drawImage(image, screenX, screenY, e.enemySize, e.enemySize, null);
        }
    }

    // Disegna il player leggendo i suoi dati
    public void drawPlayer(Graphics2D g2) {
        var p = gp.player;
        BufferedImage image = null;

        if (p.attacking) {
            boolean flip = false;
            switch (p.direction) {
                case "down":  image = p.attackDown[p.spriteNum - 1]; break;
                case "up":    image = p.attackUp[p.spriteNum - 1]; break;
                case "right": image = p.attackSide[p.spriteNum - 1]; break;
                case "left":  image = p.attackSide[p.spriteNum - 1]; flip = true; break;
            }
            if (flip) {
                g2.drawImage(image, p.screenX + gp.playerSize, p.screenY, -gp.playerSize, gp.playerSize, null);
            } else {
                g2.drawImage(image, p.screenX, p.screenY, gp.playerSize, gp.playerSize, null);
            }
            return;
        }

        switch (p.direction) {
            case "up":
                if (p.spriteNum == 1) image = p.up1;
                if (p.spriteNum == 2) image = p.up2;
                if (p.spriteNum == 3) image = p.up3;
                if (p.spriteNum == 4) image = p.up4;
                break;
            case "down":
                if (p.spriteNum == 1) image = p.down1;
                if (p.spriteNum == 2) image = p.down2;
                if (p.spriteNum == 3) image = p.down3;
                if (p.spriteNum == 4) image = p.down4;
                break;
            case "left":
                if (p.spriteNum == 1) image = p.left1;
                if (p.spriteNum == 2) image = p.left2;
                if (p.spriteNum == 3) image = p.left3;
                if (p.spriteNum == 4) image = p.left4;
                break;
            case "right":
                if (p.spriteNum == 1) image = p.right1;
                if (p.spriteNum == 2) image = p.right2;
                if (p.spriteNum == 3) image = p.right3;
                if (p.spriteNum == 4) image = p.right4;
                break;
        }
        g2.drawImage(image, p.screenX, p.screenY, gp.playerSize, gp.playerSize, null);
    }
}