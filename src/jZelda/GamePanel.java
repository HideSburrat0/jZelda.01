package jZelda;

import entity.Enemy;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {

    // Impostiamo lo schermo: ogni tile è 16x16 pixel, scalato a 48x48
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  // 768 pixel
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixel
    public final int playerSize = tileSize + 32;             // 80 pixel

    // Audio
    Sound music = new Sound();
    Sound se = new Sound();

    // World setting
    public int maxWorldCol = 70;
    public int maxWorldRow = 70;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;

    // Componenti principali del gioco
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    public GameModel model = new GameModel(this); // MODEL: stato e logica

    // Riferimenti che puntano agli oggetti del Model (così funzionano ovunque)
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = model.obj;
    public Enemy[] enemy = model.enemy;

    public UI ui = new UI(this);
    public GameView view = new GameView(this);            // VIEW: disegno
    public GameController controller = new GameController(this); // CONTROLLER: coordina il loop


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        model.player = player; // collega il player al Model
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setEnemy();
        model.gameState = model.playState;
        playMusic(0);
    }

    // Cambia mappa e riposiziona il player
    public void changeMap(int newMap, int playerCol, int playerRow) {
        tileM.changeMap(newMap);
        player.worldX = playerCol * tileSize;
        player.worldY = playerRow * tileSize;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Audio
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    public void run() {
        // Game loop con delta time: 60 FPS stabili
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                controller.updateGame(); // il Controller coordina update + repaint
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Aggiorna la logica (delega al Model)
    public void update() {
        model.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Il disegno è delegato alla View (MVC)
        view.draw(g2);

        g2.dispose();
    }
}