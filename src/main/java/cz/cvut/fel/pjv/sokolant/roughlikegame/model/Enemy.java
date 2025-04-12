package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;

public class Enemy extends Entity {
    private EnemyType type;

    public Enemy(float x, float y, float health, float damage, EnemyType type) {
        super(x, y, health, damage);
        this.type = type;
    }

    public EnemyType getType() {
        return type;
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

}
