package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

/**
 * Represents a side-scrolling camera that follows the player in the horizontal direction.
 * Saves camera position and ensures it does not scroll backwards or beyond the left screen edge.
 */
public class Camera {
    private double cameraX;
    private final double screenCenter;

    /**
     * Constructs a new Camera instance.
     *
     * @param screenCenter horizontal position representing the center of the screen (used to align the camera with the player)
     */
    public Camera(double screenCenter) {
        this.screenCenter = screenCenter;
        this.cameraX = 0;
    }

    /**
     * Updates the camera position based on the player's current X position.
     * The camera only moves forward and stays locked at 0 or higher.
     *
     * @param playerX current horizontal position of the player
     */
    public void update(double playerX) {
        double targetCameraX = playerX - screenCenter;

        if (targetCameraX > cameraX) {
            cameraX = targetCameraX;
        }

        if (cameraX < 0) {
            cameraX = 0;
        }
    }

    /**
     * Returns the current X position of the camera.
     *
     * @return current camera X coordinate
     */
    public double getX() {
        return cameraX;
    }

    /**
     * Sets the camera's X position to a specific value.
     *
     * @param cameraX new X coordinate for the camera
     */
    public void setX(double cameraX) {
        this.cameraX = cameraX;
    }
}
