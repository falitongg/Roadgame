package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ObstacleType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a destructible or animated obstacle in the game world.
 * Obstacles can have types such as boxes, fire, bottles, junk, etc.
 * They may have randomized appearances and can be damaged or animated depending on their type.
 */
public class Obstacle implements EntityDrawable {
    private int health = 50;
    private Game game;

    private float x, y;
    private float width, height;
    private Image image;
    private ObstacleType type;

    private static final Map<ObstacleType, Image[]> imageMap = new HashMap<>();
    private static final Random random = new Random();

    private Timeline animationTimeline;
    private int frameIndex = 0;

    /**
     * Constructs an obstacle with a random type at the specified position.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Obstacle(float x, float y) {
        this.x = x;
        this.y = y;

        initImages();

        ObstacleType[] types = ObstacleType.values();
        this.type = types[random.nextInt(types.length)];

        imageAppearance();

        if (type == ObstacleType.FIRE) {
            startAnimation();
        }

    }

    /**
     * Constructs an obstacle with a specific type at the specified position.
     *
     * @param x    the X coordinate
     * @param y    the Y coordinate
     * @param type the specific obstacle type
     */
    public Obstacle(float x, float y, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        initImages();
        imageAppearance();

        if (type == ObstacleType.FIRE) {
            startAnimation();
        }
    }

    public ObstacleType getType() {
        return type;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    /**
     * Returns the obstacle's rendering depth based on its Y position and type.
     * Used for depth sorting during rendering.
     */
    @Override
    public double getRenderY() {
        return switch (type) {
            case BOX, GARBAGE_BAG -> y - 117;
            case FIRE -> y - 50;
            default -> y - 140;
        };
    }

    /**
     * Renders the obstacle using the graphics context and camera offset.
     *
     * @param gc       the graphics context
     * @param cameraX  the current camera X offset
     * @param player   the player (not used here)
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.drawImage(image, x - cameraX, y, width, height);


    }

    /**
     * Sets the Y position of the obstacle.
     *
     * @param y the new Y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Initializes static image map for all obstacle types.
     * Only done once for performance.
     */
    public void initImages(){
        if (imageMap.isEmpty()) {
            imageMap.put(ObstacleType.BOX, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_32x32_1.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_32x32_2.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_32x32_3.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_32x32_4.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_32x32_5.png"))
            });

            imageMap.put(ObstacleType.BOX_SMALL, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_1.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_2.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_3.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_4.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_5.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_7.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_8.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/box_tile_16x16_9.png"))}
            );

            imageMap.put(ObstacleType.BOTTLE, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/alcohol_1.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/alcohol_2.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/alcohol_3.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/alcohol_4.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_clean.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_clean_small.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_crumpled.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_crumpled_small.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_dirty.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/water_bottle_dirty_small.png"))

            });

            imageMap.put(ObstacleType.JUNK, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/crumpled_paper_1.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/rotting_food.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/rotting_food_2.png"))

            });
            imageMap.put(ObstacleType.GARBAGE_BAG, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/garbage_bag_1.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/garbage_bag_1_i.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/garbage_bag_2.png")),
                    new Image(getClass().getResourceAsStream("/images/items/trash_assets/garbage_bag_2_i.png"))
            });
            imageMap.put(ObstacleType.FIRE, new Image[] {
                    new Image(getClass().getResourceAsStream("/images/items/fire_r.png")),
                    new Image(getClass().getResourceAsStream("/images/items/fire_l.png"))
            });
        }
    }

    /**
     * Randomly selects an image for the obstacle based on its type.
     * Adjusts the dimensions accordingly.
     */
    public void imageAppearance(){

        Image[] options = imageMap.get(type);
        this.image = options[random.nextInt(options.length)];

        this.width = (float) image.getWidth() + 5;
        this.height = (float) image.getHeight() + 5;

        if (type == ObstacleType.GARBAGE_BAG) {
            this.width += 20;
            this.height += 20;
        }
    }

    /**
     * Applies damage to the obstacle. Only boxes and small boxes can be damaged.
     *
     * @param amount the damage to apply
     */
    public void takeDamage(float amount) {
        if (type != ObstacleType.BOX && type != ObstacleType.BOX_SMALL) return;

        health -= amount;
        if (health <= 0) {
            destroy();
        }
    }

    /**
     * Removes the obstacle from the game if it is destroyable.
     */
    private void destroy() {
        if (type == ObstacleType.BOX || type == ObstacleType.BOX_SMALL || type == ObstacleType.FIRE) {

            if (game != null) {
                game.getObstacles().remove(this); // deletes from list
            }else System.out.println("error");
        }
    }

    /**
     * Links this obstacle to the game instance.
     *
     * @param game the game reference
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Starts fire animation if the obstacle is of type FIRE.
     * Cycles through animation frames indefinitely.
     */
    private void startAnimation() {
        animationTimeline = new Timeline(
                new KeyFrame
                        (Duration.seconds(0.5), e -> {
                            Image[] frames = imageMap.get(ObstacleType.FIRE);
                            frameIndex = (frameIndex + 1) % frames.length;
                            image = frames[frameIndex];
                        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

}
