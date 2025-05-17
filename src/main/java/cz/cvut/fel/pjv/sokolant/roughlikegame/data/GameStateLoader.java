package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

import com.google.gson.Gson;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ObstacleType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class responsible for loading the saved game state from JSON files.
 * Restores player state, enemies, obstacles, traders, and camera position.
 */
public class GameStateLoader {

    /**
     * Finds the most recently saved game file in the given folder.
     *
     * @param folderPath the path to the folder containing save files
     * @return absolute path to the most recent save file, or null if none found
     */
    public static String findLatestSaveFile(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("save_") && name.endsWith(".json"));

        if (files == null || files.length == 0) return null;

        File latest = files[0];
        for (File f : files) {
            if (f.getName().compareTo(latest.getName()) > 0) {
                latest = f;
            }
        }
        return latest.getAbsolutePath();
    }

    /**
     * Loads the game state from a JSON file and applies it to the current game instance.
     *
     * @param game     the game instance to populate with loaded data
     * @param filename path to the JSON save file
     * @param camera   the camera to update with saved position
     */
    public static void loadGame(Game game, String filename, Camera camera) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            GameSnapshot snapshot = gson.fromJson(reader, GameSnapshot.class);

            // Loads player data
            Player player = game.getPlayer();
            player.setX(snapshot.player.x);
            player.setY(snapshot.player.y);
            player.setHealth(snapshot.player.health);
            player.setArmor(snapshot.player.armor);
            player.setStamina(snapshot.player.stamina);
            player.setMoney(snapshot.player.money);
            player.getInventory().getItems().clear();

            if (snapshot.player.inventory != null) {
                for (InventoryItemData itemData : snapshot.player.inventory) {
                    ItemType type = ItemType.valueOf(itemData.type);
                    for (int i = 0; i < itemData.count; i++) {
                        player.getInventory().add(createItemFromType(type));
                    }
                }
            }

            // Loads enemies
            game.getEnemies().clear();
            for (EnemyData ed : snapshot.enemies) {
                Enemy enemy = switch (EnemyType.valueOf(ed.type)) {
                    case DOG -> new DogEnemy((float) ed.x, (float) ed.y);
                    case ZOMBIE -> new ZombieEnemy((float) ed.x, (float) ed.y);
                };
                enemy.setHealth(ed.health);
                game.spawnEnemy(enemy);
            }

            // Loads obstacles
            game.getObstacles().clear();
            for (ObstacleData od : snapshot.obstacles) {
                Obstacle obstacle = new Obstacle((float) od.x, (float) od.y, ObstacleType.valueOf(od.type));
                obstacle.setGame(game);
                game.getObstacles().add(obstacle);
            }

            // Loads traders
            game.getTraders().clear();
            for (TraderData td : snapshot.traderList) {
                Trader trader = new Trader((float) td.x, (float) td.y);
                game.getTraders().add(trader);
            }

            // Restores world state
            game.setLastChunkX(snapshot.lastChunkX);
            camera.setX(snapshot.cameraX);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link Item} instance based on its type.
     * Used when reconstructing inventory from saved data.
     *
     * @param type the type of item to create
     * @return a new item with predefined name and price
     */
    private static Item createItemFromType(ItemType type) {
        return switch (type) {
            case WATER -> new Item("Water", type, 50);
            case BANDAGE -> new Item("Bandage", type, 70);
            case ARMOR -> new Item("Armor", type, 100);
            case BOXER -> new Item("Knuckle", type, 300);
            case BUCKET -> new Item("Bucket", type, 0);
        };
    }
}
