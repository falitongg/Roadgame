package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Item;
import javafx.scene.image.Image;

/**
 * Enumeration representing different types of usable or collectible items in the game.
 * Includes utility methods for random loot generation and retrieving associated icons.
 */
public enum ItemType {

    /** Restores player health (+50 HP). */
    BANDAGE,

    /** Restores player stamina (+50 STAMINA). */
    WATER,

    /** Adds armor to the player (+50 ARMOR). */
    ARMOR,

    /** Equippable knuckle weapon that increases damage. */
    BOXER,

    /** Craftable item that boosts stamina and can extinguish fire. */
    BUCKET;

    // Defines which item types can be randomly spawned as loot
    private static final ItemType[] spawnable = {
            BANDAGE, WATER, ARMOR
    };

    /**
     * Returns a random lootable item type from the predefined set.
     *
     * @return randomly chosen ItemType
     */
    public static ItemType getRandom() {
        return spawnable[(int)(Math.random() * spawnable.length)];
    }

    /**
     * Returns the icon image associated with a specific item type.
     * Only predefined types are supported.
     *
     * @param type the item type to fetch the image for
     * @return Image representing the item, or null if unavailable
     */
    public static Image getImage(ItemType type) {
        switch (type) {
            case BANDAGE: return new Image(Item.class.getResourceAsStream("/images/items/bandage.png"));
            case WATER: return new Image(Item.class.getResourceAsStream("/images/items/water.png"));
            case ARMOR: return new Image(Item.class.getResourceAsStream("/images/items/armor.png"));
            default: return null;
        }
    }
}
