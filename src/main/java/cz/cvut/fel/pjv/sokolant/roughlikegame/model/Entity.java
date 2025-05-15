package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public abstract class Entity {
    protected double x;
    protected double y;
    protected float health;
    protected float damage;
    protected float width;
    protected float height;


    protected boolean isFlashing = false;

    public Entity(float x, float y, float health, float damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
    }//decreases  the entity's health
    public void takeDamage(float amount) {
        health -= amount;
        flash();
        if (this.health < 0) {
            this.health = 0;
            // TODO: die();
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getHealth() {
        return health;
    }

    public float getDamage() {
        return damage;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }
    public void flash(){
        isFlashing = true;
        new Timeline(
                new KeyFrame(
                        Duration.millis(200), e -> isFlashing = false
                )
        ).play();
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

}
