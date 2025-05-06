package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy extends Entity implements EntityDrawable {
    private EnemyType type;
    private Image sprite;

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
        //TODO реализовать получение урона врагом
    }
    public void attackPlayer(Player player) {
        // TODO уменьшить здоровье игрока на damage
    }
    public void die() {
        // TODO: логика смерти врага
    }

}
