package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;

public class BackgroundLayer {
    private Image image;
    private double parallaxFactor;

    public BackgroundLayer(Image image, double parallaxFactor) {
        this.image = image;
        this.parallaxFactor = parallaxFactor;
    }

    public void render(GraphicsContext gc, Camera camera, double viewportWidth, double viewportHeight) {
        double x = -camera.getX() * parallaxFactor;
        double imageWidth = image.getWidth();

        // Сдвигаем, чтобы фон начинался слева корректно
        x = x % imageWidth;
        if (x > 0) {
            x -= imageWidth;
        }

        // Рисуем фон, пока не закроем весь экран по ширине
        while (x < viewportWidth) {
            gc.drawImage(image, x, 0, imageWidth, viewportHeight);
            x += imageWidth;
        }
    }
}