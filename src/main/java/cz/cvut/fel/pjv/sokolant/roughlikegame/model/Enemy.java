package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;

public class Enemy {
    private float health;
    private float damage;
    private float x;
    private float y;
    private EnemyType type;

    public Enemy(EnemyType type, float y, float x, float damage, float health) {
        this.type = type;
        this.y = y;
        this.x = x;
        this.damage = damage;
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public float getDamage() {
        return damage;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public EnemyType getType() {
        return type;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public void move(float x, float y) {
        //TODO реализовать перемещение
    }
    public void takeDamage(float amount) {
        //TODO реализовать получение урона врагом
    }
    public void attackPlayer(Player player) {
        // TODO уменьшить здоровье игрока на damage
    }
    public void die() {
        // TODO: логика смерти врага
    }

    public boolean isAlive() {
        return health > 0;
    }


}
