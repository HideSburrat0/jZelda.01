package jZelda;

import entity.Enemy;
import entity.Player;
import object.SuperObject;

// MODEL: contiene lo STATO del gioco e la LOGICA che lo aggiorna.
// Non disegna e non legge direttamente la tastiera.
public class GameModel {

    GamePanel gp;

    // --- STATO DEL GIOCO ---

    // Gli stati possibili
    public final int playState = 1;
    public final int gameOverState = 2;
    public final int shopState = 3;
    public int gameState;          // stato attuale
    public int shopSelection = 0;  // item selezionato nello shop

    // Le entità del gioco (il "mondo")
    public Player player;
    public SuperObject[] obj = new SuperObject[20];
    public Enemy[] enemy = new Enemy[20];

    public GameModel(GamePanel gp) {
        this.gp = gp;
    }

    // --- LOGICA ---

    // Aggiorna tutta la logica del gioco
    public void update() {
        if (gameState == playState) {
            player.update();

            // Aggiorna gli oggetti animati (torce, baule)
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].animated) {
                    obj[i].update();
                }
            }

            // Aggiorna i nemici
            for (int i = 0; i < enemy.length; i++) {
                if (enemy[i] != null) {
                    enemy[i].update();
                }
            }
        }
    }

    // Compra l'item selezionato nello shop
    public void buyItem() {
        int price = 0;
        if (shopSelection == 0) price = 5;
        if (shopSelection == 1) price = 8;
        if (shopSelection == 2) price = 15;

        if (player.rupees >= price) {
            player.rupees -= price;

            if (shopSelection == 0) { player.heal(4); gp.playSE(5); }
            if (shopSelection == 1) { player.activateStrength(); gp.playSE(6); }
            if (shopSelection == 2) { player.hasShoes = true; gp.playSE(7); }
        }
    }

    // Riporta il gioco allo stato iniziale (ricomincia dopo il game over)
    public void restartGame() {
        player.setDefaultValues();

        for (int i = 0; i < enemy.length; i++) enemy[i] = null;
        for (int i = 0; i < obj.length; i++) obj[i] = null;

        gp.aSetter.setObject();
        gp.aSetter.setEnemy();

        gameState = playState;
    }
}
