package cz.cvut.fel.pjv.sokolant.roughlikegame.model;
import java.util.ArrayList;
import java.util.List;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;


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
        //TODO логика инициализации мира, врагов и т. п
    }
    public void update() {
        if (!player.isAlive()) {
            endGame();
        }if (currentState != GameState.PLAYING) return;
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

    }
