package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ZombieEnemy extends Enemy {
    private Image[] walkRightFrames;
    private Image[] walkLeftFrames;

    private Image zombieRight;
    private Image zombieLeft;

    private int walkFrameIndex = 0;
    private double lastStepX = -1;
    private final double stepDistance = 30;

    public ZombieEnemy(float x, float y) {
        super(x, y, 100, 15, 1.0f); // медленный, но живучий
        this.zombieRight = new Image(getClass().getResourceAsStream("/images/enemies/zombie/zombie_right.png"));
        this.zombieLeft = new Image(getClass().getResourceAsStream("/images/enemies/zombie/zombie_left.png"));

        walkRightFrames = new Image[] {
                zombieRight,
                new Image(getClass().getResourceAsStream("/images/enemies/zombie/zombie_s1_r.png")),
                zombieRight
        };

        walkLeftFrames = new Image[] {
                zombieLeft,
                new Image(getClass().getResourceAsStream("/images/enemies/zombie/zombie_s1_l.png")),
                zombieLeft
        };
    }

    @Override
    public void update(Player player) {
        double dx = player.getX() - this.getX();
        double dy = player.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (Math.abs(dx) > 5) {
            if (dx > 0) x += speed;
            else x -= speed;

            if (Math.abs(x - lastStepX) > stepDistance) {
                walkFrameIndex = (walkFrameIndex + 1) % walkRightFrames.length;
                lastStepX = x;
            }
        }

        if (distance < 50) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttackTime > 1000) {
                player.takeDamage(this.damage);
                lastAttackTime = currentTime;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.setImageSmoothing(false);

        boolean playerOnLeft = player.getX() < this.x;

        Image sprite = playerOnLeft ?
                walkLeftFrames[walkFrameIndex] : walkRightFrames[walkFrameIndex];

        gc.drawImage(sprite, (float) x - cameraX, (float) y);
    }
}
