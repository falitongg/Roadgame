package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;

public interface EntityDrawable {
    double getRenderY();
    void render(GraphicsContext gc, double cameraX, Player player);
}
