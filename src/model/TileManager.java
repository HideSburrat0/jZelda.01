package model;

import jZelda.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    public int currentMap = MapManager.OVERWORLD;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[7000];
        mapTileNum = new int[4][70][70];
        loadMap(MapManager.OVERWORLD);
    }

    // Legge la sezione "tilesets" dal JSON e carica ogni tileset col suo firstgid
    public void loadTilesetsFromJson(String json) {
        String[] parts = json.split("\"firstgid\":");

        for (int i = 1; i < parts.length; i++) {
            String block = parts[i];

            int firstgid = readNumberAtStart(block);
            String fileName = readImageName(block);

            // Colonne e numero di tile calcolati dall'immagine reale (più sicuro)
            loadTileset(fileName, firstgid);
        }
    }

    // Carica un singolo tileset calcolando colonne e numero di tile dall'immagine stessa
    public void loadTileset(String fileName, int firstgid) {
        try {
            InputStream is = getClass().getResourceAsStream("/tiles/" + fileName);
            if (is == null) {
                System.out.println("FILE NON TROVATO: " + fileName);
                return;
            }
            BufferedImage sheet = ImageIO.read(is);

            int columns = sheet.getWidth() / 16;
            int rows = sheet.getHeight() / 16;
            int tilecount = columns * rows;

            for (int i = 0; i < tilecount; i++) {
                int x = (i % columns) * 16;
                int y = (i / columns) * 16;
                tile[firstgid + i] = new Tile();
                tile[firstgid + i].image = sheet.getSubimage(x, y, 16, 16);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readImageName(String block) {
        int start = block.indexOf("\"image\":\"") + "\"image\":\"".length();
        String rest = block.substring(start);
        String fullPath = rest.split("\"")[0];
        fullPath = fullPath.replace("\\/", "/");
        String[] folders = fullPath.split("/");
        return folders[folders.length - 1];
    }

    public int readNumberAtStart(String block) {
        String rest = block.trim();
        String number = rest.split("[,}]")[0].trim();
        return Integer.parseInt(number);
    }

    public int readValue(String json, String label) {
        int start = json.indexOf(label) + label.length();
        String rest = json.substring(start).trim();
        String number = rest.split("[,}]")[0].trim();
        return Integer.parseInt(number);
    }

    public void loadMap(int mapNumber) {
        String filePath = MapManager.getPath(mapNumber);
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            String json = new String(is.readAllBytes());
            json = json.replaceAll("\\s+", " ");

            loadTilesetsFromJson(json);

            int width = readValue(json, "\"width\":");
            int height = readValue(json, "\"height\":");
            gp.maxWorldCol = width;
            gp.maxWorldRow = height;
            gp.worldWidth = gp.tileSize * width;
            gp.worldHeight = gp.tileSize * height;

            String[] layers = json.split("\"data\":\\[");

            for (int layer = 1; layer < layers.length && layer <= 4; layer++) {
                String dataStr = layers[layer].split("\\]")[0];
                String[] nums = dataStr.trim().split(",");

                for (int i = 0; i < nums.length; i++) {
                    int col = i % width;
                    int row = i / width;
                    if (row < height) {
                        mapTileNum[layer - 1][col][row] = Integer.parseInt(nums[i].trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMap(int newMap) {
        for (int layer = 0; layer < 4; layer++) {
            for (int col = 0; col < 70; col++) {
                for (int row = 0; row < 70; row++) {
                    mapTileNum[layer][col][row] = 0;
                }
            }
        }
        currentMap = newMap;
        loadMap(newMap);
    }
}