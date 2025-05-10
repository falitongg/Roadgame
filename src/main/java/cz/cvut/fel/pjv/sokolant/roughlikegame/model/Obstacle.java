package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Obstacle implements EntityDrawable {
    private float x, y;
    private float width, height;
    private Image image;

    private static Image[] images = null;

    public Obstacle(float x, float y) {
        this.x = x;
        this.y = y;

        if (images == null) {
            try {
                images = new Image[] {
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_1.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_2.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_3.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_4.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_5.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_6.png")),
                        new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/alcohol_1.png")),


                };
            } catch (Exception e) {
                System.err.println("Error loading images from file");
                e.printStackTrace();
                images = new Image[] { new Image(Obstacle.class.getResourceAsStream("/images/items/trash_assets/box_tile_32x32_1.png")) };
            }
        }

        int index = (int) (Math.random() * images.length);
        this.image = images[index];
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public void setImage(Image image) { this.image = image; }
    public Image getImage() { return image; }

    @Override
    public double getRenderY() {
        return y + height;
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.drawImage(image, x - cameraX, y);
    }
}
