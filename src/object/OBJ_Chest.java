package object;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Chest extends SuperObject {

    GamePanel gp;

    BufferedImage[] frames = new BufferedImage[4]; // i 4 frame dell'apertura
    boolean isOpened = false;
    boolean opening = false;     // true mentre si sta aprendo (animazione in corso)
    int animCounter = 0;         // conta i tick dell'animazione
    int currentFrame = 0;        // quale frame stiamo mostrando

    public OBJ_Chest(GamePanel gp) {
        this.gp = gp;

        name = "Chest";
        interactable = true; // risponde al tasto E
        animated = true;

        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/objects.png"));

            // I 4 frame: finestra fissa 20x16 per tutti (così l'animazione non "balla")
            frames[0] = sheet.getSubimage(149, 0,  20, 16); // chiuso
            frames[1] = sheet.getSubimage(197, 23, 20, 16); // poco aperto
            frames[2] = sheet.getSubimage(229, 23, 20, 16); // più aperto
            frames[3] = sheet.getSubimage(182, 0,  20, 16); // tutto aperto

            image = frames[0]; // parte chiuso
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Quando interagisci, avvia l'animazione di apertura (una volta sola)
    @Override
    public void interact() {
        if (isOpened == false) {
            opening = true;   // avvia l'animazione
            isOpened = true;  // non si può riaprire
        }
    }

    // Aggiorna l'animazione di apertura (va chiamato ogni frame)
    public void update() {
        if (opening == true) {
            animCounter++;

            // Ogni 10 tick avanza al frame successivo
            if (animCounter > 10) {
                currentFrame++;
                animCounter = 0;

                if (currentFrame >= frames.length) {
                    // Animazione finita: resta sull'ultimo frame (aperto)
                    currentFrame = frames.length - 1;
                    opening = false;
                }

                image = frames[currentFrame]; // aggiorna l'immagine mostrata
            }
        }
    }
}