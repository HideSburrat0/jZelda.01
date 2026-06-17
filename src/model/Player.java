package model;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity {

    GamePanel gp;

    public int screenX;
    public int screenY;

    // Statistiche
    public int maxLife = 8;
    public int life = 8;
    public int rupees = 0;
    public int score = 0;

    // Attacco
    public boolean attacking = false;
    public int attackCounter = 0;
    public int attackDamage = 1;
    public BufferedImage[] attackDown = new BufferedImage[4];
    public BufferedImage[] attackUp = new BufferedImage[2];
    public BufferedImage[] attackSide = new BufferedImage[3];

    // Invincibilità
    public boolean invincible = false;
    public int invincibleCounter = 0;

    // Item: scarpe
    public boolean hasShoes = false;
    public int normalSpeed = 4;
    public int runSpeed = 8;

    // Item: forza
    public boolean strengthBoost = false;
    public int strengthCounter = 0;

    public Player(GamePanel gp) {
        this.gp = gp;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 20;
        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValues();
        getPlayerImage();
        getAttackImages();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 34;
        worldY = gp.tileSize * 32;
        speed = normalSpeed;
        direction = "down";
        life = maxLife;
        rupees = 0;
        score = 0;
    }

    public void getPlayerImage() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    // UPDATE: logica che gira sempre. NON legge i tasti (lo fa il Controller).
    public void update() {
        if (attacking) {
            attackingUpdate();
            return;
        }

        checkAutoPortal();
        checkEnemyCollision();
        checkItemPickup();

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (strengthBoost) {
            strengthCounter++;
            if (strengthCounter > 600) {
                strengthBoost = false;
                attackDamage = 1;
                strengthCounter = 0;
            }
        }

        updateCamera();
    }

    // --- METODI-AZIONE (chiamati dal Controller) ---

    // Muove il player in una direzione
    public void move(String dir) {
        if (attacking) return;

        direction = dir;

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 8) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 3;
            else if (spriteNum == 3) spriteNum = 4;
            else if (spriteNum == 4) spriteNum = 1;
            spriteCounter = 0;
        }
    }

    // Inizia un attacco
    public void startAttack() {
        if (attacking) return;
        attacking = true;
        attackCounter = 0;
        spriteNum = 1;
        gp.playSE(1);
    }

    // Imposta corsa o velocità normale
    public void setRunning(boolean running) {
        if (hasShoes && running) {
            speed = runSpeed;
        } else {
            speed = normalSpeed;
        }
    }

    // Interagisce con un oggetto vicino (il Controller lo chiama quando si preme E)
    public void interact() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i].interactable) {
                int objLeft = gp.obj[i].worldX - gp.tileSize;
                int objRight = gp.obj[i].worldX + gp.obj[i].widthInTiles * gp.tileSize + gp.tileSize;
                int objTop = gp.obj[i].worldY - gp.tileSize;
                int objBottom = gp.obj[i].worldY + gp.obj[i].heightInTiles * gp.tileSize + gp.tileSize;

                if (worldX + gp.tileSize > objLeft && worldX < objRight &&
                        worldY + gp.tileSize > objTop && worldY < objBottom) {
                    gp.obj[i].interact();
                }
            }
        }
    }

    // --- LOGICA INTERNA ---

    public void updateCamera() {
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        if (worldX < screenX) screenX = worldX;
        if (worldY < screenY) screenY = worldY;

        int distanceRight = gp.worldWidth - worldX;
        if (distanceRight < gp.screenWidth - screenX) {
            screenX = gp.screenWidth - distanceRight;
        }
        int distanceBottom = gp.worldHeight - worldY;
        if (distanceBottom < gp.screenHeight - screenY) {
            screenY = gp.screenHeight - distanceBottom;
        }
    }

    public void attackingUpdate() {
        attackCounter++;
        if (attackCounter > 4) {
            spriteNum++;
            attackCounter = 0;

            int maxFrames = 4;
            if (direction.equals("up")) maxFrames = 2;
            if (direction.equals("left") || direction.equals("right")) maxFrames = 3;

            if (spriteNum == 2) {
                checkEnemyHit();
            }
            if (spriteNum > maxFrames) {
                attacking = false;
                spriteNum = 1;
            }
        }
    }

    // --- ITEM: effetti ---

    public void heal(int amount) {
        life += amount;
        if (life > maxLife) life = maxLife;
    }

    public void activateStrength() {
        strengthBoost = true;
        strengthCounter = 0;
        attackDamage = 2;
    }

    public void addHeartContainer() {
        maxLife += 2;
        life = maxLife;
    }

    // --- CONTROLLI ---

    public void checkEnemyHit() {
        int attackSize = gp.tileSize + 48;
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

                    gp.enemy[i].life -= attackDamage;
                    gp.playSE(2);

                    if (gp.enemy[i].life <= 0) {
                        score += gp.enemy[i].scoreValue;
                        dropRupee(gp.enemy[i]);
                        gp.enemy[i] = null;
                    }
                }
            }
        }
    }

    public void dropRupee(Enemy enemy) {
        int roll = (int) (Math.random() * 100);
        if (roll < enemy.dropChance) {
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    OBJ_Rupee rupee = new OBJ_Rupee(gp);
                    rupee.worldX = enemy.worldX;
                    rupee.worldY = enemy.worldY;
                    rupee.value = enemy.dropValue;
                    gp.obj[i] = rupee;
                    break;
                }
            }
        }
    }

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

                    if (!invincible) {
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

    public void checkAutoPortal() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i] instanceof OBJ_AutoPortal) {
                int objLeft = gp.obj[i].worldX;
                int objRight = gp.obj[i].worldX + gp.obj[i].widthInTiles * gp.tileSize;
                int objTop = gp.obj[i].worldY;
                int objBottom = gp.obj[i].worldY + gp.obj[i].heightInTiles * gp.tileSize;

                if (worldX + gp.tileSize > objLeft && worldX < objRight &&
                        worldY + gp.tileSize > objTop && worldY < objBottom) {
                    OBJ_AutoPortal portal = (OBJ_AutoPortal) gp.obj[i];
                    portal.activate();
                }
            }
        }
    }

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
}