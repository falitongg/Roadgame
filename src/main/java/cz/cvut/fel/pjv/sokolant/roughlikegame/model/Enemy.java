package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public abstract class Enemy extends Entity implements EntityDrawable {
    protected float speed;
    protected Image spriteLeft;
    protected Image spriteRight;
    protected long lastAttackTime = 0;
    protected float maxHealth;
    protected EnemyType type;

    protected double offsetX = 0;
    protected double offsetY = 0;

    public Enemy(float x, float y, float health, float damage, float speed, float maxHealth) {
        super(x, y, health, damage);
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.width = 160;

    }

    public abstract void update(Player player);

    public abstract void render(GraphicsContext gc, double cameraX, Player player);

    @Override
    public double getRenderY() {
        return getY();
    }

    public EnemyType getType() {
        return type;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void attackAnimation(Player player) {
        this.offsetX = (player.getX() < this.x) ? -15 : 15;

        new Timeline(
                new KeyFrame(
                        Duration.millis(120), e -> this.offsetX = 0
                )
        ).play();
    }

}
