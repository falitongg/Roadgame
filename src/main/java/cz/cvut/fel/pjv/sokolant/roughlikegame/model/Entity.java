package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public abstract class Entity {
    protected float x;
    protected float y;
    protected float health;
    protected float damage;

    public Entity(float x, float y, float health, float damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
    }
    public void takeDamage(float amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHealth() {
        return health;
    }

    public float getDamage() {
        return damage;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
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
}
