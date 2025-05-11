package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Enemy extends Entity implements EntityDrawable {
    protected float speed;
    protected Image spriteLeft;
    protected Image spriteRight;

    public Enemy(float x, float y, float health, float damage, float speed) {
        super(x, y, health, damage);
        this.speed = speed;
    }

    public abstract void update(Player player);

    public abstract void render(GraphicsContext gc, double cameraX, Player player);

    @Override
    public double getRenderY() {
        return getY();
    }
}
