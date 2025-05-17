/**
 * Represents a slow-moving zombie enemy.
 * The zombie walks toward the player and attacks in close proximity.
 * Implements basic walking animation based on direction and distance.
 */
package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
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

    /**
     * Constructs a new ZombieEnemy instance at the specified coordinates.
     *
     * @param x initial X coordinate
     * @param y initial Y coordinate
     */
    public ZombieEnemy(float x, float y) {
        super(x, y, 100, 15, 1.0f, 100);
        this.type = EnemyType.ZOMBIE;

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

    /**
     * Updates the zombie's position and attacks the player if close enough.
     * Changes animation frame as the zombie walks.
     *
     * @param player the player to chase and attack
     */
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
                attackAnimation(player);
                lastAttackTime = currentTime;
            }
        }
    }

    /**
     * Renders the zombie on screen with walking animation and flashing effect when hit.
     *
     * @param gc       graphics context to draw to
     * @param cameraX  current camera X offset
     * @param player   reference to the player (used to determine direction)
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.setImageSmoothing(false);

        boolean playerOnLeft = player.getX() < this.x;

        Image sprite = playerOnLeft ?
                walkLeftFrames[walkFrameIndex] : walkRightFrames[walkFrameIndex];

        if (isFlashing) {
            gc.setGlobalBlendMode(javafx.scene.effect.BlendMode.GREEN);
        }

        gc.drawImage(sprite, (float) x + offsetX - cameraX, (float) y);

        if (isFlashing) {
            gc.setGlobalBlendMode(javafx.scene.effect.BlendMode.SRC_OVER); //returns to normal mode
        }
    }
}