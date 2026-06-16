package jZelda;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {

    GamePanel gp;
    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage potionHeal, potionStrength, shoes; // sprite degli item per lo shop

    public UI(GamePanel gp) {
        this.gp = gp;
        getHeartImages();
        getItemImages();
    }

    // Carica gli sprite dei cuori dallo sheet (pieno, mezzo, vuoto)
    public void getHeartImages() {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/objects.png"));
            heartFull  = sheet.getSubimage(64,  0, 16, 16);
            heartHalf  = sheet.getSubimage(96,  0, 16, 16);
            heartEmpty = sheet.getSubimage(128, 0, 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carica gli sprite degli item per lo shop
    public void getItemImages() {
        try {
            // Le due pozioni nello stesso sheet (16x16 ciascuna)
            BufferedImage pozioniSheet = ImageIO.read(getClass().getResourceAsStream("/objects/Pozioni.png"));
            potionHeal = pozioniSheet.getSubimage(0, 0, 16, 16);      // pozione cura (sinistra)
            potionStrength = pozioniSheet.getSubimage(16, 0, 16, 16); // pozione forza (destra)

            // Le scarpe in un file a parte
            shoes = ImageIO.read(getClass().getResourceAsStream("/objects/Scarpe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Smista cosa disegnare in base allo stato del gioco
    public void draw(Graphics2D g2) {
        if (gp.gameState == gp.playState) {
            drawHearts(g2);
        }
        if (gp.gameState == gp.gameOverState) {
            drawGameOver(g2);
        }
        if (gp.gameState == gp.shopState) {
            drawShop(g2);
        }
    }

    // Disegna cuori, rupie e punteggio in alto a sinistra
    public void drawHearts(Graphics2D g2) {
        int x = 20;
        int y = 20;
        int heartSize = gp.tileSize;

        // Contenitori vuoti (in base alla vita massima)
        int i = 0;
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heartEmpty, x, y, heartSize, heartSize, null);
            x += heartSize;
            i++;
        }

        // Cuori pieni/mezzi (in base alla vita attuale)
        x = 20;
        i = 0;
        while (i < gp.player.life) {
            if (i + 1 < gp.player.life) {
                g2.drawImage(heartFull, x, y, heartSize, heartSize, null);
            } else {
                g2.drawImage(heartHalf, x, y, heartSize, heartSize, null);
            }
            x += heartSize;
            i += 2;
        }

        // Rupie e punteggio
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Rupie: " + gp.player.rupees, 20, 100);
        g2.drawString("Punti: " + gp.player.score, 20, 130);
    }

    // Disegna la schermata di game over
    public void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        String text = "GAME OVER";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth / 2 - textWidth / 2;
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        String text2 = "Premi INVIO per ricominciare";
        int text2Width = g2.getFontMetrics().stringWidth(text2);
        int x2 = gp.screenWidth / 2 - text2Width / 2;
        int y2 = y + 60;
        g2.drawString(text2, x2, y2);
    }

    // Disegna la schermata del negozio con le icone degli item
    public void drawShop(Graphics2D g2) {
        // Sfondo scuro
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Titolo
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("NEGOZIO", gp.screenWidth/2 - 90, 80);

        // Rupie del player
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Le tue rupie: " + gp.player.rupees, gp.screenWidth/2 - 90, 120);

        // Lista item: icona + prezzo
        BufferedImage[] icone = {potionHeal, potionStrength, shoes};
        int[] prezzi = {5, 8, 15};
        int iconSize = 48;

        int y = 180;
        for (int i = 0; i < 3; i++) {
            int x = gp.screenWidth/2 - 100;

            // Freccia gialla sull'item selezionato
            if (i == gp.shopSelection) {
                g2.setColor(Color.yellow);
                g2.setFont(new Font("Arial", Font.BOLD, 32));
                g2.drawString(">", x - 40, y + 35);
            }

            // Icona dell'item
            g2.drawImage(icone[i], x, y, iconSize, iconSize, null);

            // Prezzo accanto
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.PLAIN, 28));
            g2.drawString("- " + prezzi[i] + " rupie", x + iconSize + 20, y + 35);

            y += 70;
        }

        // Descrizione dell'item selezionato
        String[] descrizioni = {
                "Cura 2 cuori",
                "Aumenta la forza per 10 secondi",
                "Permette di correre tenendo Shift"
        };

        g2.setColor(Color.cyan);
        g2.setFont(new Font("Arial", Font.ITALIC, 22));
        String desc = descrizioni[gp.shopSelection];
        int descWidth = g2.getFontMetrics().stringWidth(desc);
        g2.drawString(desc, gp.screenWidth/2 - descWidth/2, gp.screenHeight - 120);

        // Istruzioni
        g2.setColor(Color.lightGray);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("Frecce/WS per scegliere, INVIO per comprare, ESC per uscire", gp.screenWidth/2 - 230, gp.screenHeight - 60);
    }
}