package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

/**
 * Enumeration of possible obstacle types in the game world.
 * Used to define appearance, behavior, and interaction logic of obstacles.
 */
public enum ObstacleType {

    /** Large wooden box that can be destroyed. */
    BOX,

    /** Smaller variant of the destructible box. */
    BOX_SMALL,

    /** Bottles (possibly lootable or decorative). */
    BOTTLE,

    /** Junk such as paper or rotten food (typically static). */
    JUNK,

    /** Black garbage bags (larger obstacle, non-destructible). */
    GARBAGE_BAG,

    /** Fire obstacle that deals damage to the player. */
    FIRE
}
