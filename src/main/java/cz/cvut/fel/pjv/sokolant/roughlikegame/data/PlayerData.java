package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the player's state at the time of saving.
 * Contains core attributes and inventory contents.
 */
public class PlayerData {

    /** Player's X-coordinate in the world. */
    public double x;

    /** Player's Y-coordinate in the world. */
    public double y;

    /** Current health value of the player. */
    public float health;

    /** Current armor value of the player. */
    public float armor;

    /** Current stamina value of the player. */
    public float stamina;

    /** Amount of money the player has. */
    public int money;

    /** List of items held in the player's inventory. */
    public List<InventoryItemData> inventory;
}
