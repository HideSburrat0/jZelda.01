package jZelda;



import javax.swing.*;


public class JZelda {

    public static void main (String[] args) {
// Configurazione della finestra principale
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("JZelda");
// Aggiunge il pannello di gioco alla finestra e adatta le dimensioni
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
// Centra la finestra e la rende visibile
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}