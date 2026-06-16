package jZelda;

// CONTROLLER: coordina il ciclo di gioco.
// Ad ogni frame dice al gioco di aggiornare la logica e di ridisegnare.
// Fa da tramite tra il game loop e Model/View.
public class GameController {

    GamePanel gp;

    public GameController(GamePanel gp) {
        this.gp = gp;
    }

    // Eseguito ad ogni frame dal game loop: aggiorna la logica e ridisegna
    public void updateGame() {
        gp.update();   // aggiorna la logica del gioco (per ora ancora nel GamePanel)
        gp.repaint();  // richiede il ridisegno (che la View esegue)
    }
}