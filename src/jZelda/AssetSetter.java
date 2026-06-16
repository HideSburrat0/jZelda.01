package jZelda;

import entity.ENM_Bat;
import entity.ENM_Skeleton;
import object.*;
import tile.MapManager;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Torch();
        gp.obj[0].worldX = 31 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;
        gp.obj[0].drawSize = gp.tileSize * 2;

        gp.obj[1] = new OBJ_Torch();
        gp.obj[1].worldX = 35 * gp.tileSize;
        gp.obj[1].worldY = 8 * gp.tileSize;
        gp.obj[1].drawSize = gp.tileSize * 2;

        gp.obj[2] = new OBJ_Chest(gp);
        gp.obj[2].worldX = 31 * gp.tileSize;
        gp.obj[2].worldY = 24 * gp.tileSize;
        gp.obj[2].drawSize = gp.tileSize * 2;

        // PORTALE STATUA → (dungeon da decidere)
        gp.obj[3] = new OBJ_Portal(gp, MapManager.OVERWORLD, 10, 10);
        gp.obj[3].worldX = 35 * gp.tileSize;
        gp.obj[3].worldY = 65 * gp.tileSize;

        gp.obj[4] = new OBJ_Portal(gp, MapManager.OVERWORLD, 10, 10);
        gp.obj[4].worldX = 4 * gp.tileSize;
        gp.obj[4].worldY = 65 * gp.tileSize;

        // PORTALE ALBERO (largo 4 tile) → dungeon
        gp.obj[5] = new OBJ_Portal(gp, MapManager.OVERWORLD, 10, 10);
        gp.obj[5].worldX = 64 * gp.tileSize;
        gp.obj[5].worldY = 18 * gp.tileSize;
        gp.obj[5].widthInTiles = 4;
        gp.obj[5].heightInTiles = 2;

        // ENTRATA GROTTA (automatica) → dungeon
        gp.obj[6] = new OBJ_AutoPortal(gp, MapManager.OVERWORLD, 10, 10);
        gp.obj[6].worldX = 33 * gp.tileSize;
        gp.obj[6].worldY = 8 * gp.tileSize;
        gp.obj[6].widthInTiles = 2;
        gp.obj[6].heightInTiles = 1;

        gp.obj[7] = new OBJ_AutoPortal(gp, MapManager.BRIDGE, 7, 1);
        gp.obj[7].worldX = 64 * gp.tileSize;
        gp.obj[7].worldY = 69 * gp.tileSize;
        gp.obj[7].widthInTiles = 4;
        gp.obj[7].heightInTiles = 1;

        // SHOP (bancarella)
        gp.obj[8] = new OBJ_Shop(gp);
        gp.obj[8].worldX = 25 * gp.tileSize;
        gp.obj[8].worldY = 22 * gp.tileSize;
        gp.obj[8].widthInTiles = 4;
        gp.obj[8].heightInTiles = 2;
    }

    public void setEnemy() {
        // Crea scheletri in posizioni casuali
        for (int i = 0; i < 30; i++) {
            spawnEnemyRandom("skeleton");
        }
    }

    // Crea un nemico del tipo richiesto in una posizione casuale valida
    public void spawnEnemyRandom(String type) {

        // Cerca uno slot libero nell'array dei nemici
        int slot = -1;
        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) return; // array pieno, non spawna

        // Prova a trovare una posizione casuale valida (max 100 tentativi)
        for (int tentativi = 0; tentativi < 100; tentativi++) {
            int col = (int)(Math.random() * gp.maxWorldCol); // colonna a caso
            int row = (int)(Math.random() * gp.maxWorldRow); // riga a caso

            // La cella deve essere libera (non muro)
            if (gp.cChecker.cellaLibera(col, row)) {

                // Distanza dal player (non spawnare troppo vicino)
                int playerCol = gp.player.worldX / gp.tileSize;
                int playerRow = gp.player.worldY / gp.tileSize;
                int distCol = Math.abs(col - playerCol);
                int distRow = Math.abs(row - playerRow);

                // Almeno 5 celle di distanza dal player
                if (distCol > 5 || distRow > 5) {

                    // Crea il nemico del tipo giusto
                    if (type.equals("skeleton")) {
                        gp.enemy[slot] = new ENM_Skeleton(gp);
                    } else if (type.equals("bat")) {
                        gp.enemy[slot] = new ENM_Bat(gp);
                    }

                    // Posiziona il nemico nella cella trovata
                    gp.enemy[slot].worldX = col * gp.tileSize;
                    gp.enemy[slot].worldY = row * gp.tileSize;

                    return; // nemico piazzato, esci
                }
            }
        }
        // Se dopo 100 tentativi non trova posto, semplicemente non spawna questo nemico
    }
}