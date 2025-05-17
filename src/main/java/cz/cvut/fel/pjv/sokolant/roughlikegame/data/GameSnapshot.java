package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

import java.util.List;

/**
 * Represents a snapshot of the game state used for saving and loading.
 * Captures all necessary data to reconstruct the game world on reload.
 */
public class GameSnapshot {

    /** Saved state of the player. */
    public PlayerData player;

    /** List of all enemies and their state at the time of saving. */
    public List<EnemyData> enemies;

    /** List of all obstacles present in the world. */
    public List<ObstacleData> obstacles;

    /** The X-coordinate of the last generated chunk. */
    public float lastChunkX;

    /** The X-offset of the camera at the time of saving. */
    public double cameraX;

    /** List of all traders in the world. */
    public List<TraderData> traderList;
}
