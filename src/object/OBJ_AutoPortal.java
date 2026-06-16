package object;

import jZelda.GamePanel;

public class OBJ_AutoPortal extends SuperObject {

    GamePanel gp;
    int targetMap;
    int targetCol;
    int targetRow;

    public OBJ_AutoPortal(GamePanel gp, int targetMap, int targetCol, int targetRow) {
        this.gp = gp;
        this.targetMap = targetMap;
        this.targetCol = targetCol;
        this.targetRow = targetRow;

        name = "AutoPortal";
        // Non è interactable: si attiva calpestandolo, non col tasto E
    }

    // Attiva il teletrasporto (chiamato quando il player calpesta l'area)
    public void activate() {
        gp.changeMap(targetMap, targetCol, targetRow);
    }
}