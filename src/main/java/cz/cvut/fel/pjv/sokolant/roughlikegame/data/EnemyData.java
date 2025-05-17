package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

/**
 * Data Transfer Object (DTO) for saving and loading enemy state.
 * Used during serialization of the game world.
 */
public class EnemyData {

    /** Type of the enemy (e.g., ZOMBIE, DOG) represented as string. */
    public String type;

    /** X coordinate of the enemy's position. */
    public double x;

    /** Y coordinate of the enemy's position. */
    public double y;

    /** Current health of the enemy. */
    public float health;
}
