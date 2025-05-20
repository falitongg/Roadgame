package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;

/**
 * Represents the core game logic, managing player state, enemy spawning,
 * obstacle generation, game state transitions, and interactions.
 */
public class Game {

    private Player player;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private final List<Item> items = new ArrayList<>();
    private final List<Trader> traders = new ArrayList<>();
    private Trader currentTrader = null;
    private GameState currentState;
    private float lastChunkX = 0;

    /**
     * Initializes a new game instance with a default player and empty game elements.
     */
    public Game() {
        this.player = new Player();
        this.player.setGame(this);
        this.enemies = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.currentState = GameState.MENU;
    }

    /**
     * Starts the game by changing its state to PLAYING.
     */
    public void startGame() {
        currentState = GameState.PLAYING;
    }

    /**
     * Updates the game elements including player, enemies, obstacles,
     * and handles their lifecycle during gameplay.
     *
     * @param cameraX Current horizontal position of the camera.
     */
    public void update(double cameraX) {
        if (currentState != GameState.PLAYING) return;

        player.update();

        for (Enemy enemy : enemies) {
            enemy.update(player);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            boolean dead = !e.isAlive();
            boolean offScreen = e.getX() + e.getWidth() < cameraX - 500;

            if (dead && e.getType() == EnemyType.ZOMBIE) {
                player.addMoney(15);
            }

            if (dead || offScreen) {
                it.remove();
            }
        }

        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() < cameraX - 200);
        traders.removeIf(t -> t.getX() + t.getWidth() < cameraX - 500);

        if (!player.isAlive()) {
            endGame();
        }
    }

    /**
     * Ends the game by setting the game state to GAME_OVER and stopping player regeneration.
     */
    public void endGame() {
        player.setRegenRunning(false);
        currentState = GameState.GAME_OVER;
    }

    public void spawnEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Generates a new game chunk including enemies, obstacles, and events.
     *
     * @param startX Starting horizontal coordinate for generation.
     * @param endX Ending horizontal coordinate for generation.
     */
    public void generateChunk(float startX, float endX) {
        generateEnemies(startX, endX);
        generateObstacles(startX, endX);}

    /**
     * Generates enemy entities within the specified horizontal range.
     *
     * Iterates over the given X-range in 200-pixel steps and spawns an enemy
     * at each step with a 30% probability. The enemy is placed at a random
     * vertical position within a predefined range, and its type is randomly
     *
     * @param startX    the starting horizontal coordinate of the chunk
     * @param endX      the ending horizontal coordinate of the chunk
     */
    private void generateEnemies(float startX, float endX) {
        Random rand = new Random();
        for (float x = startX; x <= endX; x += 200) {
            if (rand.nextFloat() < 0.3f) {
                float y = 467 + rand.nextFloat() * (560 - 467);
                Enemy enemy = rand.nextBoolean() ? new DogEnemy(x, y) : new ZombieEnemy(x, y);
                spawnEnemy(enemy);
            }
        }
    }

    /**
     * Generates obstacles within the specified horizontal range and occasionally spawns a trader.
     *
     * Iterates through the X-range in 150-pixel steps, creating an obstacle at each position.
     * The vertical position of each obstacle is randomized based on its type.
     * Additionally, with a 1% probability and if no trader exists in the current chunk,
     * a trader is spawned at the given location.
     *
     * @param startX
     * @param endX
     */
    private void generateObstacles(float startX, float endX) {
        Random rand = new Random();
        for (float x = startX; x <= endX; x += 150) {
            if (rand.nextFloat() < 0.01f && traders.stream().noneMatch(t -> t.getX() >= startX && t.getX() <= endX)) {
                spawnTrader(x);
            }

            Obstacle obstacle = new Obstacle(x, 0);
            obstacle.setGame(this);

            float minY, maxY;
            switch (obstacle.getType()) {
                case GARBAGE_BAG, JUNK, BOTTLE -> { minY = 602; maxY = 640; }
                case BOX, BOX_SMALL -> { minY = 600; maxY = 690; }
                default -> { minY = 580; maxY = 640; }
            }
            obstacle.setY(minY + rand.nextFloat() * (maxY - minY));
            obstacles.add(obstacle);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public GameState getState() {
        return currentState;
    }

    public void setState(GameState state) {
        this.currentState = state;
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

    public List<Trader> getTraders() {
        return traders;
    }

    private void spawnTrader(float spawnX) {
        float groundY = 420;
        traders.add(new Trader(spawnX, groundY));
    }

    public Trader getCurrentTrader() {
        return currentTrader;
    }

    public void setCurrentTrader(Trader t) {
        currentTrader = t;
    }

    public void spawnItem(float x, float y) {
        ItemType randomType = ItemType.getRandom();
        Item item = new Item("?", randomType, 0);
        item.setPosition(x, y);
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
