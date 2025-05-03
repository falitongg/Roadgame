package cz.cvut.fel.pjv.sokolant.roughlikegame.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Player;

public class Game {
    private Player player;
    private List<Enemy> enemies;
    private GameState currentState;

    public Game() {
        this.player = new Player();
        this.enemies = new ArrayList<>();
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
        //TODO логика инициализации мира, врагов и т. п
    }
    public void update() {
        if (currentState != GameState.PLAYING) return;
        player.update();

        if (!player.isAlive()) {
            endGame();
        }
        //TODO логика тиков, проверка столкновений и урона от врагов
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
            if (rand.nextFloat() < 0.9f) {
                EnemyType type = EnemyType.values()[rand.nextInt(EnemyType.values().length)];
                float minY = 590;
                float maxY = 500;
                float y = minY + rand.nextFloat() * (maxY - minY);
                Enemy enemy = new Enemy(x, y, 100, 10, type);
                spawnEnemy(enemy);
            }

        }
    }
    public void generateObstacles(float startX, float endX) {

    }
    public void generateEvents(float startX, float endX) {

    }
}