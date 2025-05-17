package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;

/**
 * Represents a fast, agile enemy of type DOG.
 * Implements AI behavior such as sprinting toward the player when far,
 * slowing down when closer, and attacking in proximity.
 * Includes frame-based animation for walking and sprinting in both directions.
 */
public class DogEnemy extends Enemy {

    private Image[] walkRightFrames;
    private Image[] walkLeftFrames;
    private Image[] runRightFrames;
    private Image[] runLeftFrames;

    private int walkFrameIndex = 0;
    private double lastStepX = -1;
    private final double stepDistance = 6;

    private final float walkSpeed = 2.5f;
    private final float sprintSpeed = 5.0f;

    private boolean isSprinting = false;

    /**
     * Constructs a new DogEnemy at the specified position.
     *
     * @param x initial X coordinate
     * @param y initial Y coordinate
     */
    public DogEnemy(float x, float y) {
        super(x, y, 50, 10, 2.5f, 50);
        this.type = EnemyType.DOG;

        // Load standing sprites
        spriteRight = new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_r.png"));
        spriteLeft  = new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_l.png"));

        // Load walking frames
        walkRightFrames = new Image[] {
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s2_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s3_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s4_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s5_r.png"))
        };

        walkLeftFrames = new Image[] {
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s2_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s3_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s4_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s5_l.png"))
        };

        // Load sprinting frames
        runRightFrames = new Image[] {
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_1_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_2_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_3_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_4_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_5_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_6_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_7_r.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_8_r.png"))
        };

        runLeftFrames = new Image[] {
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_1_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_2_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_3_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_4_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_5_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_6_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_7_l.png")),
                new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_sprint_8_l.png"))
        };
    }

    /**
     * Updates the DogEnemy's behavior based on the player's position.
     * Sprints when far from the player, walks when close, attacks when very close.
     *
     * @param player the player character to target
     */
    @Override
    public void update(Player player) {
        double dx = player.getX() - this.getX();
        double dy = player.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 300) {
            speed = sprintSpeed;
            isSprinting = true;
        } else {
            speed = walkSpeed;
            isSprinting = false;
        }

        if (Math.abs(dx) > 5) {
            this.x += dx > 0 ? speed : -speed;
        }

        if (distance < 50) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttackTime > 1000) {
                player.takeDamage(this.damage);
                attackAnimation(player);
                lastAttackTime = currentTime;
            }
        }

        if (Math.abs(x - lastStepX) > stepDistance) {
            walkFrameIndex = (walkFrameIndex + 1) % walkRightFrames.length;
            lastStepX = x;
        }
    }

    /**
     * Renders the DogEnemy based on its movement and state.
     * Chooses the correct animation frame and direction. Adds flashing effect if damaged.
     *
     * @param gc       the graphics context for rendering
     * @param cameraX  current X position of the camera for offset
     * @param player   the player (used for directional orientation)
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.setImageSmoothing(false);
        boolean playerOnLeft = player.getX() < this.x;
        Image spriteToDraw;

        if (isSprinting) {
            spriteToDraw = playerOnLeft ?
                    runLeftFrames[walkFrameIndex % runLeftFrames.length] :
                    runRightFrames[walkFrameIndex % runRightFrames.length];
        } else {
            spriteToDraw = playerOnLeft ?
                    walkLeftFrames[walkFrameIndex % walkLeftFrames.length] :
                    walkRightFrames[walkFrameIndex % walkRightFrames.length];
        }

        if (isFlashing) {
            gc.setGlobalBlendMode(BlendMode.RED);
        }

        gc.drawImage(spriteToDraw, (float) x + offsetX - cameraX, (float) y);

        if (isFlashing) {
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        }
    }

    /**
     * Returns the Y coordinate used for depth sorting in rendering.
     *
     * @return Y position of the enemy
     */
    @Override
    public double getRenderY() {
        return getY();
    }
}
