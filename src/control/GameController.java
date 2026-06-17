package control;

import jZelda.GamePanel;

// CONTROLLER: legge gli input e comanda il Model. Coordina il ciclo di gioco.
public class GameController {

    GamePanel gp;

    public GameController(GamePanel gp) {
        this.gp = gp;
    }

    // Eseguito ad ogni frame dal game loop
    public void updateGame() {
        handleInput();   // legge i tasti e comanda il player
        gp.update();     // aggiorna la logica del gioco (Model)
        gp.repaint();    // ridisegna (View)
    }

    // Legge i tasti e dice al player cosa fare (solo in playState)
    public void handleInput() {
        if (gp.model.gameState != gp.model.playState) {
            return; // negli altri stati (shop, game over) non si comanda il player
        }

        var keyH = gp.keyH;
        var player = gp.player;

        // Corsa (Shift) o velocità normale
        player.setRunning(keyH.shiftPressed);

        // Attacco (spazio)
        if (keyH.attackPressed) {
            player.startAttack();
        }

        // Movimento (WASD) - una direzione per frame
        if (keyH.upPressed) {
            player.move("up");
        } else if (keyH.downPressed) {
            player.move("down");
        } else if (keyH.leftPressed) {
            player.move("left");
        } else if (keyH.rightPressed) {
            player.move("right");
        }

        // Interazione (E)
        if (keyH.ePressed) {
            player.interact();
            keyH.ePressed = false;
        }
    }
}