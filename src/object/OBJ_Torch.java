package object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Torch extends SuperObject {

    public OBJ_Torch() {

        name = "Torch";
        animated = true; // questa torcia usa l'animazione a 3 frame

        try {
            // Carica lo sheet e ritaglia i 3 frame (32x32) a y=320, x=96/128/160
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/2_indoors.png"));
            frame1 = sheet.getSubimage(96,  320, 32, 32);
            frame2 = sheet.getSubimage(128, 320, 32, 32);
            frame3 = sheet.getSubimage(160, 320, 32, 32);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
