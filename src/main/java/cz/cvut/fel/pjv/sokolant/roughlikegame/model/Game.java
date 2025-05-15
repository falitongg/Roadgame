package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;

public class Game {
    private Player player;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private final List<Trader> traders = new ArrayList<>();
    private Trader currentTrader = null;
    private GameState currentState;
    private float lastChunkX = 0; // ← перенесено сюда

    public Game() {
        this.player = new Player();
        this.player.setGame(this);
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.currentState = GameState.MENU;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void startGame(){
        currentState = GameState.PLAYING;
    }

    public void update(double cameraX) {
        if (currentState != GameState.PLAYING) return;

        player.update();

        for (Enemy enemy : enemies) {
            enemy.update(player); // AI of enemies
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();

            boolean dead      = !e.isAlive();
            boolean offScreen = e.getX() + e.getWidth() < cameraX - 500;  // запас 500 px

            if (dead && e.getType() == EnemyType.ZOMBIE) {
                player.addMoney(15);
                System.out.println(player.getMoney());
            }

            if (dead || offScreen) {
                it.remove();
            }
        }


        obstacles.removeIf(obstacle ->
                obstacle.getX() + obstacle.getWidth() < cameraX - 200
        );

        traders.removeIf(t ->
                t.getX() + t.getWidth() < cameraX - 500
        );

        if (!player.isAlive()) {
            endGame();
        }
    }

    public GameState getState() {
        return currentState;
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void endGame() {
        currentState = GameState.GAME_OVER;
    }

    public void spawnEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void generateChunk(float startX, float endX) {
        generateEnemies(startX, endX);
        generateObstacles(startX, endX);
        generateEvents(startX, endX);
    }

    public void generateEnemies(float startX, float endX) {
        Random rand = new Random();
        for (float x = startX; x <= endX; x += 200) {
            if (rand.nextFloat() < 0.3f) {
                float minY = 467;
                float maxY = 560;
                float y = minY + rand.nextFloat() * (maxY - minY);
                int typeIndex = rand.nextInt(2);
                Enemy enemy = switch (typeIndex) {
                    case 0 -> new DogEnemy(x, y);
                    case 1 -> new ZombieEnemy(x, y);
                    default -> null;
                };
                if (enemy != null) {
                    spawnEnemy(enemy);
                }
            }
        }
    }

    public void generateObstacles(float startX, float endX) {
        Random rand = new Random();
        for (float x = startX; x <= endX; x += 150) {
            if (rand.nextFloat() < 0.1f) {
                boolean traderAlreadyExists = traders.stream()
                        .anyMatch(t -> t.getX() >= startX && t.getX() <= endX);

                if (!traderAlreadyExists) {
                    spawnTrader(x);
                }

            }
                Obstacle obstacle = new Obstacle(x, 0);
                obstacle.setGame(this);


            float minY, maxY;
                switch (obstacle.getType()) {
                    case GARBAGE_BAG, JUNK, BOTTLE -> {
                        minY = 467 + 125 + 10;
                        maxY = 500 + 130 + 10;
                    }
                    case BOX, BOX_SMALL -> {
                        minY = 600;
                        maxY = 690;
                    }
                    default -> {
                        minY = 580;
                        maxY = 640;
                    }
                }
                float y = minY + rand.nextFloat() * (maxY - minY);
                obstacle.setY(y);
                obstacles.add(obstacle);
            }
    }

    public void generateEvents(float startX, float endX) {
        // future events here
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public float getLastChunkX() {
        return lastChunkX;
    }

    public void setLastChunkX(float lastChunkX) {
        this.lastChunkX = lastChunkX;
    }
    public List<Trader> getTraders() { return traders; }

    private void spawnTrader(float spawnX) {
        float groundY = 420;
        traders.add(new Trader(spawnX, groundY));
    }
    public Trader getCurrentTrader() { return currentTrader; }
    public void setCurrentTrader(Trader t) { currentTrader = t; }
}