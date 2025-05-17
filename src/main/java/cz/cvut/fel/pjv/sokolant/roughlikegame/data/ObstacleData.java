package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

/**
 * Data Transfer Object (DTO) representing an obstacle in the saved game state.
 * Stores obstacle type and its position for reconstruction during loading.
 */
public class ObstacleData {

    /** Type of the obstacle (must match {@link cz.cvut.fel.pjv.sokolant.roughlikegame.util.ObstacleType}). */
    public String type;

    /** X-coordinate of the obstacle's position. */
    public double x;

    /** Y-coordinate of the obstacle's position. */
    public double y;
}
