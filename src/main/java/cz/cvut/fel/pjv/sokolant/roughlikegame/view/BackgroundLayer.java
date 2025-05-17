package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;

/**
 * Represents a single background layer in a parallax-scrolling environment.
 * Each layer moves at a different speed based on its parallax factor,
 * creating a depth effect as the camera moves.
 */
public class BackgroundLayer {
    private Image image;
    private double parallaxFactor;

    /**
     * Constructs a new background layer with a specified image and parallax factor.
     *
     * @param image           the image to be repeated across the background
     * @param parallaxFactor  the movement multiplier (lower = slower, for distant layers)
     */
    public BackgroundLayer(Image image, double parallaxFactor) {
        this.image = image;
        this.parallaxFactor = parallaxFactor;
    }

    /**
     * Renders the background layer onto the canvas.
     * The image is repeated horizontally to fill the viewport and offset according to camera position.
     *
     * @param gc              graphics context to draw onto
     * @param camera          camera object providing current X offset
     * @param viewportWidth   width of the visible area
     * @param viewportHeight  height of the visible area
     */
    public void render(GraphicsContext gc, Camera camera, double viewportWidth, double viewportHeight) {
        double x = -camera.getX() * parallaxFactor;
        double imageWidth = image.getWidth();

        x = x % imageWidth;
        if (x > 0) {
            x -= imageWidth;
        }

        while (x < viewportWidth) {
            gc.drawImage(image, x, 0, imageWidth, viewportHeight);
            x += imageWidth;
        }
    }
}
