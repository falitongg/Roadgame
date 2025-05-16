package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GameStateSaver {

    public static void saveGame(Game game, String filename, double cameraX) {
        GameSnapshot snapshot = new GameSnapshot();

        // saves player
        Player player = game.getPlayer();
        PlayerData pd = new PlayerData();
        pd.x = player.getX();
        pd.y = player.getY();
        pd.health = player.getHealth();
        pd.armor = player.getArmor();
        pd.stamina = player.getStamina();
        pd.money = player.getMoney();
        pd.inventory = new ArrayList<>();
        for(Map.Entry<ItemType, Integer> entry : player.getInventory().getItems().entrySet()) {
            InventoryItemData itemData = new InventoryItemData();
            itemData.type = entry.getKey().name();
            itemData.count = entry.getValue();
            pd.inventory.add(itemData);
        }
        snapshot.player = pd;

        // saves enemies
        snapshot.enemies = new ArrayList<>();
        for (Enemy enemy : game.getEnemies()) {
            EnemyData ed = new EnemyData();
            ed.type = enemy.getType().name();
            ed.x = enemy.getX();
            ed.y = enemy.getY();
            ed.health = enemy.getHealth();
            snapshot.enemies.add(ed);
            snapshot.cameraX = cameraX;
        }

        // saves obstacles
        snapshot.obstacles = new ArrayList<>();
        for (Obstacle o : game.getObstacles()) {
            ObstacleData od = new ObstacleData();
            od.type = o.getType().name();
            od.x = o.getX();
            od.y = o.getY();
            snapshot.obstacles.add(od);
            snapshot.lastChunkX = game.getLastChunkX();
        }

        //saves trader position
        snapshot.traderList = new ArrayList<>();
        for (Trader trader : game.getTraders()) {
            TraderData td = new TraderData();
            td.x = trader.getX();
            td.y = trader.getY();
            snapshot.traderList.add(td);
        }
        //saves inventory

        // writes to the file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = new File(filename);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();  // создаёт все необходимые папки (например, saves/)
        }

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(snapshot, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved to: " + new java.io.File(filename).getAbsolutePath());

    }
}