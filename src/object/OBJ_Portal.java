package object;

import jZelda.GamePanel;

public class OBJ_Portal extends SuperObject {

    GamePanel gp;
    int targetMap;
    int targetCol;
    int targetRow;

    public OBJ_Portal(GamePanel gp, int targetMap, int targetCol, int targetRow) {
        this.gp = gp;
        this.targetMap = targetMap;
        this.targetCol = targetCol;
        this.targetRow = targetRow;

        name = "Portal";
        interactable = true;
    }

    @Override
    public void interact() {
        gp.changeMap(targetMap, targetCol, targetRow);
    }
}