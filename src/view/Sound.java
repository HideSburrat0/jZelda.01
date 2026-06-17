package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;                       // il "lettore" del suono
    URL[] soundURL = new URL[30];    // elenco dei file audio

    public Sound() {
        // Carica i percorsi dei file audio (adatta i nomi ai tuoi file)
        soundURL[0] = getClass().getResource("/sound/music_overworld.wav");      // musica di sottofondo
        soundURL[1] = getClass().getResource("/sound/sword.wav");      // colpo di spada
        soundURL[2] = getClass().getResource("/sound/hit.wav");        // nemico colpito
        soundURL[3] = getClass().getResource("/sound/rupee.wav");      // rupia raccolta
        soundURL[4] = getClass().getResource("/sound/damage.wav");     // danno subito
        soundURL[5] = getClass().getResource("/sound/healing.wav"); // pozione di cura
        soundURL[6] = getClass().getResource("/sound/boost.wav"); // pozione forza
        soundURL[7] = getClass().getResource("/sound/upgrade.wav"); // acquisto di scarpe
    }

    // Prepara il suono indicato (lo "carica" nel lettore)
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fa partire il suono una volta
    public void play() {
        clip.start();
    }

    // Fa ripetere il suono all'infinito (per la musica di sottofondo)
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Ferma il suono
    public void stop() {
        clip.stop();
    }
}