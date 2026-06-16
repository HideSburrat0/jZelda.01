package jZelda;

import java.awt.*;

// VIEW: si occupa SOLO di disegnare lo stato del gioco sullo schermo.
// Non modifica i dati, non legge input. Legge dal GamePanel e disegna.
public class GameView {

    GamePanel gp;

    public GameView(GamePanel gp) {
        this.gp = gp;
    }

    // Disegna tutto il gioco nell'ordine corretto
    public void draw(Graphics2D g2) {

        // 1. La mappa (tile)
        gp.tileM.draw(g2);

        // 2. Gli oggetti (torce, baule, portali, shop)
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                gp.obj[i].draw(g2, gp);
            }
        }

        // 3. I nemici
        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] != null) {
                gp.enemy[i].draw(g2);
            }
        }

        // 4. Il player
        gp.player.draw(g2);

        // 5. L'interfaccia (cuori, rupie, shop, game over)
        gp.ui.draw(g2);
    }
}