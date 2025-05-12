package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Enemy extends Entity implements EntityDrawable {
    protected float speed;
    protected Image spriteLeft;
    protected Image spriteRight;
    protected long lastAttackTime = 0;
    protected float maxHealth;
    protected EnemyType type;

    public Enemy(float x, float y, float health, float damage, float speed, float maxHealth) {
        super(x, y, health, damage);
        this.speed = speed;
        this.maxHealth = maxHealth;
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

}
