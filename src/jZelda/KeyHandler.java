package jZelda;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Gestisce gli input da tastiera del giocatore
public class KeyHandler implements KeyListener {

    GamePanel gp; // riferimento al GamePanel per accedere allo stato del gioco

    // Stato dei tasti: true se premuto, false se rilasciato
    public boolean upPressed, downPressed, leftPressed, rightPressed, ePressed, attackPressed, shiftPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            attackPressed = true;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }

        // Comandi nello shop
        if (gp.gameState == gp.shopState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.shopSelection--;
                if (gp.shopSelection < 0) gp.shopSelection = 2; // torna in fondo
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.shopSelection++;
                if (gp.shopSelection > 2) gp.shopSelection = 0; // torna in cima
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.buyItem(); // compra l'item selezionato
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState; // esce dallo shop
            }
        }

        // In game over: INVIO fa ricominciare
        if (gp.gameState == gp.gameOverState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            attackPressed = false;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }
}