package cz.cvut.fel.pjv.sokolant.roughlikegame.data;

/**
 * Data Transfer Object (DTO) for storing item type and quantity in the player's inventory.
 * Used when saving and loading game state via JSON.
 */
public class InventoryItemData {

    /** The name of the item type (must match {@link cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType}). */
    public String type;

    /** Number of items of the specified type. */
    public int count;
}
