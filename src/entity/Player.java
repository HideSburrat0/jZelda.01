package entity;
import jZelda.GamePanel;
import jZelda.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public int screenX;
    public int screenY;

    // Statistiche del player
    public int maxLife = 8;            // vita massima (8 = 4 cuori)
    public int life = 8;               // vita attuale
    public int rupees = 0;             // monete raccolte (per lo shop)
    public int score = 0;              // punteggio (per la classifica)

    // Stato dell'attacco
    public boolean attacking = false;
    public int attackCounter = 0;
    public int attackDamage = 1;       // danno della spada (aumenta col boost di forza)
    public BufferedImage[] attackDown = new BufferedImage[4];
    public BufferedImage[] attackUp = new BufferedImage[2];
    public BufferedImage[] attackSide = new BufferedImage[3];

    // Stato invincibilità (dopo aver subito un colpo)
    public boolean invincible = false;
    public int invincibleCounter = 0;

    // Item: scarpe da corsa
    public boolean hasShoes = false;
    public int normalSpeed = 4;
    public int runSpeed = 8;

    // Item: potenziamento di forza temporaneo
    public boolean strengthBoost = false;
    public int strengthCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Rettangolo che definisce l'area soggetta a collisione
        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 20;
        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValues();
        getPlayerImage();
        getAttackImages();
    }

    public void setDefaultValues(){
        // Posizione, velocità e direzione iniziale
        worldX = gp.tileSize * 34;
        worldY = gp.tileSize * 32;
        speed = normalSpeed;
        direction = "down";
        life = maxLife;
        rupees = 0;
        score = 0;
    }

    // Carica gli sprite del player per ogni direzione (4 frame per direzione)
    public void getPlayerImage(){
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/Link Up0.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/Link Up1.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/Link Up2.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/player/Link Up3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/Link Down0.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/Link Down1.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/Link Down2.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/player/Link Down3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/Link Left0.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/Link Left1.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/Link Left2.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/Link Left3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/Link Right0.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/Link Right1.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/Link Right2.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/Link Right3.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Carica gli sprite delle animazioni d'attacco (frame diversi per direzione)
    public void getAttackImages() {
        try {
            BufferedImage down = ImageIO.read(getClass().getResourceAsStream("/attack/attack_down.png"));
            BufferedImage up = ImageIO.read(getClass().getResourceAsStream("/attack/attack_up.png"));
            BufferedImage side = ImageIO.read(getClass().getResourceAsStream("/attack/attack_side.png"));

            for (int i = 0; i < 4; i++) attackDown[i] = down.getSubimage(i * 16, 0, 16, 16);
            for (int i = 0; i < 2; i++) attackUp[i] = up.getSubimage(i * 16, 0, 16, 16);
            for (int i = 0; i < 3; i++) attackSide[i] = side.getSubimage(i * 16, 0, 16, 16);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        // Se sta attaccando, gestisce l'animazione e blocca il resto
        if (attacking == true) {
            attackingUpdate();
            return;
        }

        // Spazio: inizia l'attacco
        if (keyH.attackPressed == true) {
            attacking = true;
            attackCounter = 0;
            spriteNum = 1;
            gp.playSE(1);  // suono spada
            return;
        }

        // Se ha le scarpe e preme Shift, corre
        if (hasShoes && keyH.shiftPressed) {
            speed = runSpeed;
        } else {
            speed = normalSpeed;
        }

        // Movimento in base ai tasti premuti
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Controllo collisioni col mondo
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Si muove solo se non c'è collisione
            if (collisionOn == false) {
                switch (direction) {
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "left":  worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // Anima i 4 frame della camminata
            spriteCounter++;
            if (spriteCounter > 8) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 4;
                } else if (spriteNum == 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        // Controlli che girano sempre, anche da fermo
        checkInteraction();
        checkAutoPortal();
        checkEnemyCollision();
        checkItemPickup();

        // Timer dell'invincibilità (~1 secondo dopo un colpo)
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Timer del potenziamento di forza (~10 secondi)
        if (strengthBoost == true) {
            strengthCounter++;
            if (strengthCounter > 600) {
                strengthBoost = false;
                attackDamage = 1; // torna al danno normale
                strengthCounter = 0;
            }
        }

        // Limiti camera: il player resta al centro tranne vicino ai bordi della mappa
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        if (worldX < screenX) {
            screenX = worldX;
        }
        if (worldY < screenY) {
            screenY = worldY;
        }
        int distanceRight = gp.worldWidth - worldX;
        if (distanceRight < gp.screenWidth - screenX) {
            screenX = gp.screenWidth - distanceRight;
        }
        int distanceBottom = gp.worldHeight - worldY;
        if (distanceBottom < gp.screenHeight - screenY) {
            screenY = gp.screenHeight - distanceBottom;
        }
    }

    // Avanza l'animazione d'attacco e applica il danno al momento giusto
    public void attackingUpdate() {
        attackCounter++;

        if (attackCounter > 4) {
            spriteNum++;
            attackCounter = 0;

            // Numero di frame per direzione (giù=4, su=2, lato=3)
            int maxFrames = 4;
            if (direction.equals("up")) maxFrames = 2;
            if (direction.equals("left") || direction.equals("right")) maxFrames = 3;

            // La spada fa danno al 2° frame
            if (spriteNum == 2) {
                checkEnemyHit();
            }

            // Fine animazione: smette di attaccare
            if (spriteNum > maxFrames) {
                attacking = false;
                spriteNum = 1;
            }
        }
    }

    // --- ITEM: effetti (usati dallo shop e dai drop) ---

    // Cura il player (senza superare la vita massima)
    public void heal(int amount) {
        life += amount;
        if (life > maxLife) {
            life = maxLife;
        }
    }

    // Attiva il potenziamento di forza temporaneo (danno raddoppiato)
    public void activateStrength() {
        strengthBoost = true;
        strengthCounter = 0;
        attackDamage = 2;
    }

    // Aumenta la vita massima di un cuore intero (e cura tutta la vita)
    public void addHeartContainer() {
        maxLife += 2;
        life = maxLife;
    }

    // --- CONTROLLI ---

    // Controlla se la spada colpisce un nemico durante l'attacco
    public void checkEnemyHit() {

        int attackSize = gp.tileSize + 48; // raggio d'attacco ampio

        // Posizione della zona di danno davanti al player
        int attackX = worldX;
        int attackY = worldY;

        switch (direction) {
            case "up":    attackY = worldY - attackSize; break;
            case "down":  attackY = worldY + gp.tileSize; break;
            case "left":  attackX = worldX - attackSize; break;
            case "right": attackX = worldX + gp.tileSize; break;
        }

        int attackLeft = attackX;
        int attackRight = attackX + attackSize;
        int attackTop = attackY;
        int attackBottom = attackY + attackSize;

        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] != null) {

                int enemyLeft = gp.enemy[i].worldX + gp.enemy[i].solidArea.x;
                int enemyRight = gp.enemy[i].worldX + gp.enemy[i].solidArea.x + gp.enemy[i].solidArea.width;
                int enemyTop = gp.enemy[i].worldY + gp.enemy[i].solidArea.y;
                int enemyBottom = gp.enemy[i].worldY + gp.enemy[i].solidArea.y + gp.enemy[i].solidArea.height;

                if (attackRight > enemyLeft && attackLeft < enemyRight &&
                        attackBottom > enemyTop && attackTop < enemyBottom) {

                    gp.enemy[i].life -= attackDamage; // danno (aumenta col boost)
                    gp.playSE(2);

                    // Se la vita arriva a 0, il nemico muore
                    if (gp.enemy[i].life <= 0) {
                        score += gp.enemy[i].scoreValue;
                        dropRupee(gp.enemy[i]); // possibile drop di rupia
                        gp.enemy[i] = null;
                    }
                }
            }
        }
    }

    // Forse genera una rupia dove muore il nemico (in base alla sua dropChance)
    public void dropRupee(Enemy enemy) {
        int roll = (int)(Math.random() * 100);

        if (roll < enemy.dropChance) {
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    object.OBJ_Rupee rupee = new object.OBJ_Rupee(gp);
                    rupee.worldX = enemy.worldX;
                    rupee.worldY = enemy.worldY;
                    rupee.value = enemy.dropValue;
                    gp.obj[i] = rupee;
                    break;
                }
            }
        }
    }

    // Controlla se il player preme E vicino a un oggetto interagibile
    public void checkInteraction() {

        if (keyH.ePressed) {

            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] != null && gp.obj[i].interactable) {

                    int objLeft = gp.obj[i].worldX;
                    int objRight = gp.obj[i].worldX + gp.obj[i].widthInTiles * gp.tileSize;
                    int objTop = gp.obj[i].worldY;
                    int objBottom = gp.obj[i].worldY + gp.obj[i].heightInTiles * gp.tileSize;

                    objLeft   -= gp.tileSize;
                    objRight  += gp.tileSize;
                    objTop    -= gp.tileSize;
                    objBottom += gp.tileSize;

                    if (worldX + gp.tileSize > objLeft && worldX < objRight &&
                            worldY + gp.tileSize > objTop && worldY < objBottom) {
                        gp.obj[i].interact();
                    }
                }
            }
            keyH.ePressed = false;
        }
    }

    // Controlla il contatto coi nemici (subisce danno)
    public void checkEnemyCollision() {

        for (int i = 0; i < gp.enemy.length; i++) {
            if (gp.enemy[i] != null) {

                int playerLeft = worldX + solidArea.x;
                int playerRight = worldX + solidArea.x + solidArea.width;
                int playerTop = worldY + solidArea.y;
                int playerBottom = worldY + solidArea.y + solidArea.height;

                int enemyLeft = gp.enemy[i].worldX + gp.enemy[i].solidArea.x;
                int enemyRight = gp.enemy[i].worldX + gp.enemy[i].solidArea.x + gp.enemy[i].solidArea.width;
                int enemyTop = gp.enemy[i].worldY + gp.enemy[i].solidArea.y;
                int enemyBottom = gp.enemy[i].worldY + gp.enemy[i].solidArea.y + gp.enemy[i].solidArea.height;

                if (playerRight > enemyLeft && playerLeft < enemyRight &&
                        playerBottom > enemyTop && playerTop < enemyBottom) {

                    if (invincible == false) {
                        life -= gp.enemy[i].damage;
                        gp.playSE(4);
                        invincible = true;
                        invincibleCounter = 0;
                        if (life <= 0) {
                            gp.model.gameState = gp.model.gameOverState;
                        }
                    }
                }
            }
        }
    }

    // Controlla se il player calpesta un portale automatico (grotta, ponte)
    public void checkAutoPortal() {

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i] instanceof object.OBJ_AutoPortal) {

                int objLeft = gp.obj[i].worldX;
                int objRight = gp.obj[i].worldX + gp.obj[i].widthInTiles * gp.tileSize;
                int objTop = gp.obj[i].worldY;
                int objBottom = gp.obj[i].worldY + gp.obj[i].heightInTiles * gp.tileSize;

                if (worldX + gp.tileSize > objLeft && worldX < objRight &&
                        worldY + gp.tileSize > objTop && worldY < objBottom) {

                    object.OBJ_AutoPortal portal = (object.OBJ_AutoPortal) gp.obj[i];
                    portal.activate();
                }
            }
        }
    }

    // Controlla se il player raccoglie una rupia
    public void checkItemPickup() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i].name.equals("Rupee")) {

                int objLeft = gp.obj[i].worldX;
                int objRight = gp.obj[i].worldX + gp.tileSize;
                int objTop = gp.obj[i].worldY;
                int objBottom = gp.obj[i].worldY + gp.tileSize;

                if (worldX + gp.tileSize > objLeft && worldX < objRight &&
                        worldY + gp.tileSize > objTop && worldY < objBottom) {

                    rupees += gp.obj[i].value;
                    gp.playSE(3);
                    gp.obj[i] = null;
                }
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        // Se sta attaccando, mostra lo sprite dell'attacco
        if (attacking == true) {
            boolean flip = false;
            switch (direction) {
                case "down":  image = attackDown[spriteNum - 1]; break;
                case "up":    image = attackUp[spriteNum - 1]; break;
                case "right": image = attackSide[spriteNum - 1]; break;
                case "left":  image = attackSide[spriteNum - 1]; flip = true; break;
            }

            if (flip) {
                g2.drawImage(image, screenX + gp.playerSize, screenY, -gp.playerSize, gp.playerSize, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.playerSize, gp.playerSize, null);
            }
            return;
        }

        // Sprite normale in base a direzione e frame
        switch (direction){
            case "up":
                if(spriteNum == 1){ image = up1; }
                if(spriteNum == 2){ image = up2; }
                if(spriteNum == 3){ image = up3; }
                if(spriteNum == 4){ image = up4; }
                break;
            case "down":
                if(spriteNum == 1){ image = down1; }
                if(spriteNum == 2){ image = down2; }
                if(spriteNum == 3){ image = down3; }
                if(spriteNum == 4){ image = down4; }
                break;
            case "left":
                if(spriteNum == 1){ image = left1; }
                if(spriteNum == 2){ image = left2; }
                if(spriteNum == 3){ image = left3; }
                if(spriteNum == 4){ image = left4; }
                break;
            case "right":
                if(spriteNum == 1){ image = right1; }
                if(spriteNum == 2){ image = right2; }
                if(spriteNum == 3){ image = right3; }
                if(spriteNum == 4){ image = right4; }
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.playerSize, gp.playerSize, null);
    }
}