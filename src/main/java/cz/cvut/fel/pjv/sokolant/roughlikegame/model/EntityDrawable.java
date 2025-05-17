package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for game entities that can be rendered on screen.
 * Implementing classes must define how the entity is drawn
 * and where it should appear in the rendering depth (Z-order).
 */
public interface EntityDrawable {

    /**
     * Returns the vertical coordinate used to determine rendering order.
     * Entities with lower Y-values are drawn behind those with higher values.
     *
     * @return Y coordinate used for rendering depth sorting
     */
    double getRenderY();

    /**
     * Renders the entity using the provided graphics context and camera offset.
     *
     * @param gc       graphics context to draw onto
     * @param cameraX  current horizontal camera offset
     * @param player   reference to the player (used for contextual rendering if needed)
     */
    void render(GraphicsContext gc, double cameraX, Player player);
}
