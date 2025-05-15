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

    @Override
    public double getRenderY() {
        return switch (type) {
            case BOX, GARBAGE_BAG -> y - 117;
            case FIRE -> y - 50;
            default -> y - 140;
        };
    }





    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.drawImage(image, x - cameraX, y, width, height);


    }
    public void setY(float y) {
        this.y = y;
    }

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
    public void imageAppearance(){

        Image[] options = imageMap.get(type);
        this.image = options[random.nextInt(options.length)];

        this.width = (float) image.getWidth() + 5;
        this.height = (float) image.getHeight() + 5;

        if (type == ObstacleType.GARBAGE_BAG) {
//            this.y -= 10;
            this.width += 20;
            this.height += 20;
        }
    }
    public void takeDamage(float amount) {
        if (type != ObstacleType.BOX && type != ObstacleType.BOX_SMALL) return;

        health -= amount;
        if (health <= 0) {
            destroy();
        }
    }

    private void destroy() {
        if (type == ObstacleType.BOX || type == ObstacleType.BOX_SMALL || type == ObstacleType.FIRE) {
            if (type == ObstacleType.BOX || type == ObstacleType.BOX_SMALL)System.out.println("Коробка разрушена!");
            else if (type == ObstacleType.FIRE) System.out.println("огонь потушен");
            if (game != null) {
                game.getObstacles().remove(this); // удаляем из списка
            }else System.out.println("error");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }
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
