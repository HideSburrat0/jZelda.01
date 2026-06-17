package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public int worldX, worldY;
    public int speed;
    public int actionLockCounter = 0;

//  Sprite per ogni direzione di movimento (4 frame per direzione)
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;

    public String direction;
    public String name;
//  Contatore per gestire il cambio di frame dell'animazione
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public boolean collisionOn = false;


}
