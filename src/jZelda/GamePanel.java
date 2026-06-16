package jZelda;

import entity.Enemy;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel  implements Runnable {

    // Impostiamo lo schermo : ogni tile è 16x16 pixel, scalato a 48x48
    final int originalTileSize = 16;
    final int scale = 3 ;
    public final int tileSize = originalTileSize * scale;
    public final int  maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixel
    public final int screenHeight = tileSize * maxScreenRow;// 576 pixel
    public final int playerSize = tileSize + 32; // 80pixel invece di 48
    public int gameState;              // stato attuale del gioco
    public final int playState = 1;    // gioco in corso
    public final int gameOverState = 2; // schermata game over
    Sound music = new Sound();  // per la musica di sottofondo
    Sound se = new Sound();     // per gli effetti sonori (sound effects)
    public final int shopState = 3; // schermata negozio
    public int shopSelection = 0;    // quale item è selezionato (0, 1, 2)

//    Wordl setting
    public  int maxWorldCol = 70;
    public  int maxWorldRow = 70;
    public  int worldWidth = tileSize * maxWorldCol;
    public  int worldHeight = tileSize * maxWorldRow;

    //    FPS
    int  FPS = 60;
    //  Componenti principali del gioco
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this,keyH);
    public SuperObject obj [] = new SuperObject[20];
    public Enemy[] enemy = new Enemy[20]; // i nemici della mappa corrente
    public UI ui = new UI(this);
    public GameView view = new GameView(this); // VIEW: gestisce il disegno


    public  GamePanel (){
//  Configura il pannello: dimensioni, sfondo e gestione input
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame(){
        aSetter.setObject();
        aSetter.setEnemy();
        gameState = playState;
        playMusic(0);
    }
    // Cambia mappa e riposiziona il player nel punto indicato
    public void changeMap(int newMap, int playerCol, int playerRow) {
        tileM.changeMap(newMap);
        player.worldX = playerCol * tileSize;
        player.worldY = playerRow * tileSize;
    }

    public  void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();


    }
    // Fa partire la musica di sottofondo in loop
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    // Ferma la musica
    public void stopMusic() {
        music.stop();
    }

    // Riproduce un effetto sonoro una volta
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    public void run(){
//  Game loop con sistema delta time: aggiorna e ridisegna a 60 FPS stabili
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null ){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/ drawInterval;
            timer +=(currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
//  Stampa gli FPS ogni secondo per monitorare le prestazioni
            if (timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }

    }
    public void update() {
        if (gameState == playState) {
            player.update();

            // Aggiorna l'animazione degli oggetti animati (es. torce)
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].animated) {
                    obj[i].update();
                }
            }
            for (int i = 0; i < enemy.length; i++) {
                if (enemy[i] != null) {
                    enemy[i].update();
                }

            }
        }
    }
    // Compra l'item selezionato nello shop (se ci sono abbastanza rupie)
    public void buyItem() {
        int price = 0;

        if (shopSelection == 0) price = 5;  // pozione cura
        if (shopSelection == 1) price = 8;  // pozione forza
        if (shopSelection == 2) price = 15; // scarpe

        if (player.rupees >= price) {
            player.rupees -= price; // paga

            // Applica l'effetto e suona il suono dedicato all'item
            if (shopSelection == 0) {
                player.heal(4);
                playSE(5); // suono healing
            }
            if (shopSelection == 1) {
                player.activateStrength();
                playSE(6); // suono boost
            }
            if (shopSelection == 2) {
                player.hasShoes = true;
                playSE(7); // suono upgrade
            }
        }
        // Se non ha abbastanza rupie, non succede nulla
    }
    // Riporta il gioco allo stato iniziale (usato per ricominciare dopo il game over)
    public void restartGame() {
        // Ripristina la vita del player
        player.setDefaultValues();

        // Svuota e ricrea i nemici e gli oggetti
        for (int i = 0; i < enemy.length; i++) {
            enemy[i] = null;
        }
        for (int i = 0; i < obj.length; i++) {
            obj[i] = null;
        }
        aSetter.setObject();
        aSetter.setEnemy();

        // Torna a giocare
        gameState = playState;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // Il disegno è delegato alla View (MVC)
        view.draw(g2);

        g2.dispose();
    }
}
