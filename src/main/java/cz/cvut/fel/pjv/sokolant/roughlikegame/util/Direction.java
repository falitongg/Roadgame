package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

/**
 * Enumeration representing possible movement directions for entities.
 * Used for handling player and enemy movement, orientation, and jumping.
 */
public enum Direction {
    /** Move upward */
    UP,
    /** Move downward */
    DOWN,
    /** Move left */
    LEFT,
    /** Move right */
    RIGHT,
    /** Jump or spacebar-related action */
    SPACE
}
