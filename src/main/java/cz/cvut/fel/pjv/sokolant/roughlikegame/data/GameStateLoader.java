package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

import com.google.gson.Gson;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ObstacleType;

import java.io.FileReader;
import java.io.IOException;

public class GameStateLoader {

    public static void loadGame(Game game, String filename, Camera camera) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            GameSnapshot snapshot = gson.fromJson(reader, GameSnapshot.class);

            // loads player
            Player player = game.getPlayer();
            player.setX(snapshot.player.x);
            player.setY(snapshot.player.y);
            player.setHealth(snapshot.player.health);
            player.setArmor(snapshot.player.armor);
            player.setStamina(snapshot.player.stamina);
            player.setMoney(snapshot.player.money);
            // inventory пока не трогаем

            // loads enemies
            game.getEnemies().clear();
            for (EnemyData ed : snapshot.enemies) {
                Enemy enemy = switch (EnemyType.valueOf(ed.type)) {
                    case DOG -> new DogEnemy((float) ed.x, (float) ed.y);
                    case ZOMBIE -> new ZombieEnemy((float) ed.x, (float) ed.y);
                };
                enemy.setHealth(ed.health);
                game.spawnEnemy(enemy);
            }

            // loads obstacles
            game.getObstacles().clear();
            for (ObstacleData od : snapshot.obstacles) {
                Obstacle obstacle = new Obstacle((float) od.x, (float) od.y, ObstacleType.valueOf(od.type));
                game.getObstacles().add(obstacle);
            }
            // loads traders
            game.getTraders().clear();
            for (TraderData td : snapshot.traderList) {
                Trader trader = new Trader((float) td.x, (float) td.y);
                game.getTraders().add(trader);
            }

            game.setLastChunkX(snapshot.lastChunkX);
            camera.setX(snapshot.cameraX);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
