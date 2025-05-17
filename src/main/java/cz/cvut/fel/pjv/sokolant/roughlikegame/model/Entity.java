package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Represents a generic entity in the game world.
 * Provides shared properties such as position, health, and damage.
 * Can be extended by specific types of entities like players, enemies, or NPCs.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected float health;
    protected float damage;
    protected float width;
    protected float height;

    protected boolean isFlashing = false;

    /**
     * Constructs a new entity with specified position, health, and damage.
     *
     * @param x      initial X coordinate
     * @param y      initial Y coordinate
     * @param health initial health value
     * @param damage damage the entity can deal
     */
    public Entity(float x, float y, float health, float damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
    }

    /**
     * Reduces entity's health by the given amount and triggers flash effect.
     *
     * @param amount damage to apply
     */
    public void takeDamage(float amount) {
        health -= amount;
        flash();
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Checks if the entity is still alive.
     *
     * @return true if health is greater than zero, false otherwise
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Temporarily triggers a flashing effect, typically after taking damage.
     */
    public void flash() {
        isFlashing = true;
        new Timeline(
                new KeyFrame(
                        Duration.millis(200), e -> isFlashing = false
                )
        ).play();
    }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public double getX() { return x; }

    public double getY() { return y; }

    public float getHealth() { return health; }

    public float getDamage() { return damage; }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public void setHealth(float health) { this.health = health; }

}
