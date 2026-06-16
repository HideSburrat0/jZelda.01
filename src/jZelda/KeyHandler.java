package jZelda;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Gestisce gli input da tastiera del giocatore
public class KeyHandler implements KeyListener {

    GamePanel gp;

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

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
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

        // Comandi nello shop (lo stato è nel Model)
        if (gp.model.gameState == gp.model.shopState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.model.shopSelection--;
                if (gp.model.shopSelection < 0) gp.model.shopSelection = 2;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.model.shopSelection++;
                if (gp.model.shopSelection > 2) gp.model.shopSelection = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.model.buyItem();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.model.gameState = gp.model.playState;
            }
        }

        // In game over: INVIO fa ricominciare
        if (gp.model.gameState == gp.model.gameOverState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.model.restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
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