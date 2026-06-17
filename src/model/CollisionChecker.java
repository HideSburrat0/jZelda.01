package model;

import jZelda.GamePanel;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // Controlla se l'entità sta per toccare un tile-muro nel layer collisioni (layer 3)
    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (fuoriMappa(entityLeftCol, entityTopRow) || fuoriMappa(entityRightCol, entityTopRow)) {
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum[3][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[3][entityRightCol][entityTopRow];
                if (tileNum1 != 0 || tileNum2 != 0) entity.collisionOn = true;
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (fuoriMappa(entityLeftCol, entityBottomRow) || fuoriMappa(entityRightCol, entityBottomRow)) {
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum[3][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[3][entityRightCol][entityBottomRow];
                if (tileNum1 != 0 || tileNum2 != 0) entity.collisionOn = true;
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (fuoriMappa(entityLeftCol, entityTopRow) || fuoriMappa(entityLeftCol, entityBottomRow)) {
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum[3][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[3][entityLeftCol][entityBottomRow];
                if (tileNum1 != 0 || tileNum2 != 0) entity.collisionOn = true;
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (fuoriMappa(entityRightCol, entityTopRow) || fuoriMappa(entityRightCol, entityBottomRow)) {
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum[3][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[3][entityRightCol][entityBottomRow];
                if (tileNum1 != 0 || tileNum2 != 0) entity.collisionOn = true;
                break;
        }
    }

    // Verifica se una cella è fuori dai confini della mappa
    public boolean fuoriMappa(int col, int row) {
        return col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow;
    }
    // Verifica se una cella è libera (non muro e dentro la mappa) → utile per lo spawn
    public boolean cellaLibera(int col, int row) {
        if (fuoriMappa(col, row)) {
            return false; // fuori mappa = non libera
        }
        // Sul layer 3, qualsiasi valore diverso da 0 è un muro
        return gp.tileM.mapTileNum[3][col][row] == 0;
    }
}