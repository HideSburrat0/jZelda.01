package entity;

import jZelda.GamePanel;

import java.util.Random;

public class ENM_Skeleton extends Enemy {

    Random random = new Random();

    public ENM_Skeleton(GamePanel gp) {
        super(gp); // richiama il costruttore di Enemy (imposta solidArea)

        name = "Skeleton";
        speed = 1;       // più lento del player (che va a 4)
        life = 3;
        damage = 2;
        scoreValue = 3;
        direction = "down";
        enemySize = gp.tileSize + 24;
        dropChance = 45;  // 45% di lasciare una rupia
        dropValue = 2;    // vale 1 rupia

        // Carica gli sprite dai 3 file (su, giù, lato)
        getEnemyImage(
                "/enemy/skeleton_up.png",
                "/enemy/skeleton_down.png",
                "/enemy/skeleton_side.png"
        );
    }

    // Comportamento: vaga a caso cambiando direzione ogni tanto
    @Override
    public void setAction() {
        actionLockCounter++;

        // Ogni 120 frame (~2 secondi) sceglie una nuova direzione a caso
        if (actionLockCounter == 120) {
            int i = random.nextInt(4); // numero da 0 a 3

            if (i == 0) direction = "up";
            if (i == 1) direction = "down";
            if (i == 2) direction = "left";
            if (i == 3) direction = "right";

            actionLockCounter = 0; // azzera il contatore
        }
    }
}
