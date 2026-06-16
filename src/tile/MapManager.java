package tile;

// Raccoglie in un unico posto i percorsi di tutte le mappe del gioco.
// Se cambi il nome di un file mappa, lo modifichi solo qui.
public class MapManager {

    // Nomi parlanti per ogni mappa (li usi al posto dei numeri)
    public static final int OVERWORLD = 0;
    public static final int DUNGEON_1 = 1;
    public static final int DUNGEON_2 = 2;
    public static final int DUNGEON_3 = 3;
    public static final int DUNGEON_4 = 4;
    public static final int BRIDGE = 5;
    public static final int DUNGEON_6 = 6;
    public static final int DUNGEON_7 = 7;
    public static final int FINAL_BOSS = 8;

    // Lista dei percorsi: l'ordine corrisponde ai numeri qui sopra
    public static final String[] PATHS = {
            "/maps/mappa mondo aggiornata.json",  // 0 = OVERWORLD
            "/maps/dungeon1.json",                // 1 = DUNGEON_1
            "/maps/dungeon2.json",                // 2 = DUNGEON_2
            "/maps/dungeon3.json",                // 3 = DUNGEON_3
            "/maps/dungeon4.json",                // 4 = DUNGEON_4
            "/maps/BRIDGE.json",                  // 5 = BRIDGE
            "/maps/dungeon6.json",                // 6 = DUNGEON_6
            "/maps/dungeon7.json",                // 7 = DUNGEON_7
            "/maps/finalboss.json"                // 8 = FINAL_BOSS
    };

    // Dato il numero di una mappa, restituisce il suo percorso file
    public static String getPath(int mapNumber) {
        return PATHS[mapNumber];
    }
}