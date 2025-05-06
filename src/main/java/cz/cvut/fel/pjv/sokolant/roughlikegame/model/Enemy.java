package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy extends Entity implements EntityDrawable {
    private EnemyType type;
    private Image sprite;

    private float speed = 1.2f;

    public Enemy(float x, float y, float health, float damage, EnemyType type) {
        super(x, y, health, damage);
        this.type = type;
        loadSprite();
    }

    private void loadSprite() {
        switch (type) {
            case ZOMBIE -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
            case DOG -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
            case BANDIT -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
            case MUTANT -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
            case BOSS -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
            case ANIMAL -> sprite = new Image(getClass().getResourceAsStream("/images/enemies/zombie_calm.png"));
        }
    }
    public void render(GraphicsContext gc, double cameraX) {
        gc.setImageSmoothing(false);

        if (sprite != null) {
            gc.drawImage(sprite, (float) x - cameraX, (float) y, 80, 100);
        } else {
            gc.fillRect((float) x - cameraX, (float) y, 40, 40);
        }
    }
    @Override
    public double getRenderY() {
        return getY() + 150;
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
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }
    public void attackPlayer(Player player) {
        player.takeDamage(this.damage);
    }
    public void die() {
        // TODO: логика смерти врага
    }
    public void update(Player player) {
        double dx = player.getX() - this.getX();
        if (Math.abs(dx) > 10) {
            if (dx > 0) {
                this.x += speed;
            } else {
                this.x -= speed;
            }
        } else {
            attackPlayer(player);
        }
    }
}
