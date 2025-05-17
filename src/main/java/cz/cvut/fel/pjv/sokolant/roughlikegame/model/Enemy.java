package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Represents a generic enemy entity in the game world.
 * Enemy is an abstract subclass of {@link Entity} and implements {@link EntityDrawable}.
 * All concrete enemy types (e.g., Zombie, Dog) should extend this class and implement behavior.
 */
public abstract class Enemy extends Entity implements EntityDrawable {
    protected float speed;
    protected Image spriteLeft;
    protected Image spriteRight;
    protected long lastAttackTime = 0;
    protected float maxHealth;
    protected EnemyType type;

    protected double offsetX = 0;
    protected double offsetY = 0;

    /**
     * Constructs a new enemy instance with specified parameters.
     *
     * @param x         initial X coordinate
     * @param y         initial Y coordinate
     * @param health    initial health value
     * @param damage    amount of damage this enemy can deal
     * @param speed     movement speed of the enemy
     * @param maxHealth maximum health of the enemy
     */
    public Enemy(float x, float y, float health, float damage, float speed, float maxHealth) {
        super(x, y, health, damage);
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.width = 160;
    }

    /**
     * Updates the enemy's logic, typically AI behavior based on the player's position.
     *
     * @param player the player to interact with
     */
    public abstract void update(Player player);

    /**
     * Renders the enemy sprite on the screen.
     *
     * @param gc       graphics context to draw to
     * @param cameraX  horizontal offset of the camera
     * @param player   reference to the player (used for orientation or interaction rendering)
     */
    public abstract void render(GraphicsContext gc, double cameraX, Player player);

    /**
     * Returns the Y position used for rendering depth sorting.
     *
     * @return Y position of the enemy
     */
    @Override
    public double getRenderY() {
        return getY();
    }

    /**
     * Returns the type of this enemy.
     *
     * @return enemy type enum value
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * Returns the maximum health value of the enemy.
     *
     * @return maximum health
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * Executes a brief visual attack animation offset (e.g., lunge effect).
     *
     * @param player the target player (used to determine direction of the lunge)
     */
    public void attackAnimation(Player player) {
        this.offsetX = (player.getX() < this.x) ? -15 : 15;

        new Timeline(
                new KeyFrame(
                        Duration.millis(120), e -> this.offsetX = 0
                )
        ).play();
    }
}
