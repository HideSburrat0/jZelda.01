package object;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Rupee extends SuperObject {

    GamePanel gp;
    BufferedImage[] frames = new BufferedImage[4]; // i 4 frame della rotazione
    int aniCounter = 0;   // conta i frame per l'animazione
    int aniNum = 0;       // quale frame mostrare

    int lifeTimer = 300;  // dopo 300 frame (~5 sec) la rupia sparisce

    public OBJ_Rupee(GamePanel gp) {
        this.gp = gp;
        name = "Rupee";
        value = 1; // quanto vale questa rupia
        animated = true; // così il GamePanel chiama il suo update()

        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/objects.png"));
            // 4 frame affiancati sulla riga y=64
            for (int i = 0; i < 4; i++) {
                frames[i] = sheet.getSubimage(i * 16, 64, 16, 16);
            }
            image = frames[0]; // immagine iniziale
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Aggiorna l'animazione e il timer di scomparsa
    @Override
    public void update() {
        // Animazione: cambia frame ogni 10 tick
        aniCounter++;
        if (aniCounter > 10) {
            aniNum++;
            if (aniNum > 3) aniNum = 0;
            image = frames[aniNum];
            aniCounter = 0;
        }

        // Timer di scomparsa
        lifeTimer--;
        // Negli ultimi ~1.5 secondi (90 frame) lampeggia
        if (lifeTimer < 90) {
            // Alterna visibile/invisibile ogni 5 frame
            if (lifeTimer % 15 < 7.5) {
                visible = true;
            } else {
                visible = false;
            }
        }

        // Quando il timer finisce, la rupia sparisce
        if (lifeTimer <= 0) {
            removeSelf();
        }
    }

    // Rimuove questa rupia dall'array degli oggetti
    public void removeSelf() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) {
                gp.obj[i] = null;
            }
        }
    }
}