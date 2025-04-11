package cz.cvut.fel.pjv.sokolant.roughlikegame.model;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<Enemy> enemies;
    private boolean gameOver;

    public Game() {
        this.player = new Player();
        this.enemies = new ArrayList<>();
        this.gameOver = false;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void startGame(){
        //TODO логика инициализации мира, врагов и т. п
    }
    public void update() {
        if (!player.isAlive()) {
            endGame();
        }
        //TODO логика тиков, проверка столкновений и урона от врагов
    }
    public void endGame() {
        gameOver = true;
    }
    public void spawnEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    }
