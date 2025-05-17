package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

/**
 * Represents the visual state of an entity, primarily used for animation control.
 * Indicates whether the entity is idle, moving, or reacting to being hit.
 */
public enum VisualState {

    /** The entity is not performing any action (default pose). */
    IDLE,

    /** The entity is currently moving (walking, running, etc.). */
    MOVING,

    /** The entity has been hit and is showing a reaction (e.g., flash, recoil). */
    HIT
}
