package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

    public DogEnemy(float x, float y) {
        super(x, y, 50, 10, 2.5f);
        spriteRight= new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_r.png"));
        spriteLeft = new Image(getClass().getResourceAsStream("/images/enemies/dog/dog_s1_l.png"));
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
            this.x += dx > 0 ?
                    speed : -speed;
        }

//        if (dy < -40) {
//            this.y -= 10;
//        }

        if (Math.sqrt(dx * dx + dy * dy) < 50) {
            player.takeDamage(this.damage);
        }

        if (Math.abs(x - lastStepX) > stepDistance) {
            walkFrameIndex = (walkFrameIndex + 1) % walkRightFrames.length;
            lastStepX = x;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.setImageSmoothing(false);

        boolean playerOnLeft = player.getX() < this.x;
        Image spriteToDraw;

        if (isSprinting) {
            spriteToDraw = playerOnLeft ?
                    runLeftFrames[walkFrameIndex % runLeftFrames.length] : runRightFrames[walkFrameIndex % runRightFrames.length];
        } else {
            spriteToDraw = playerOnLeft ?
                    walkLeftFrames[walkFrameIndex % walkLeftFrames.length] : walkRightFrames[walkFrameIndex % walkRightFrames.length];
        }

        gc.drawImage(spriteToDraw, (float) x - cameraX, (float) y);
    }
    @Override
    public double getRenderY() {
        return getY();
    }


}
