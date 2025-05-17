package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the player's inventory.
 * Manages item counts, adding, removing, crafting, and retrieving item icons.
 */
public class Inventory {

    private final Map<ItemType, Integer> items;

    private static final Map<ItemType, Image> itemsIcons = Map.of(
            ItemType.BANDAGE, new Image(Inventory.class.getResourceAsStream("/images/items/bandage.png")),
            ItemType.WATER, new Image(Inventory.class.getResourceAsStream("/images/items/water.png")),
            ItemType.ARMOR, new Image(Inventory.class.getResourceAsStream("/images/items/armor.png")),
            ItemType.BOXER, new Image(Inventory.class.getResourceAsStream("/images/items/boxer.png")),
            ItemType.BUCKET, new Image(Inventory.class.getResourceAsStream("/images/items/water_bucket.png"))
    );

    /**
     * Returns the icon image associated with the specified item type.
     *
     * @param type the type of item
     * @return corresponding icon image, or null if type is null or not found
     */
    public static Image getIcon(ItemType type) {
        if (type == null) return null;
        return itemsIcons.get(type);
    }

    /**
     * Constructs a new empty inventory.
     */
    public Inventory() {
        this.items = new HashMap<>();
    }

    /**
     * Adds the specified item to the inventory.
     * If the item is a knuckle (BOXER) and one is already present, it is not added.
     *
     * @param item the item to add
     */
    public void add(Item item) {
        ItemType type = item.getType();
        if (type == ItemType.BOXER && hasItem(ItemType.BOXER)) {
            return;
        }
        items.put(type, items.getOrDefault(type, 0) + 1);
    }

    /**
     * Removes one unit of the specified item type from the inventory.
     * If the count reaches zero, the item type is removed.
     *
     * @param type the item type to remove
     */
    public void remove(ItemType type) {
        if (items.containsKey(type)) {
            int count = items.get(type);
            if (count > 1) {
                items.put(type, count - 1);
            } else {
                items.remove(type);
            }
        }
    }

    /**
     * Returns the number of items of a given type in the inventory.
     *
     * @param type the item type to check
     * @return number of items of the given type
     */
    public int getCount(ItemType type) {
        return items.getOrDefault(type, 0);
    }

    /**
     * Checks whether the inventory contains at least one of the given item type.
     *
     * @param type the item type to check
     * @return true if item exists, false otherwise
     */
    public boolean hasItem(ItemType type) {
        return items.getOrDefault(type, 0) > 0;
    }

    /**
     * Returns a reference to the internal map of items.
     *
     * @return the item map
     */
    public Map<ItemType, Integer> getItems() {
        return items;
    }

    /**
     * Crafts a bucket from three units of WATER.
     * Reduces water count by 3 and adds one BUCKET.
     *
     * @return true if crafting was successful, false otherwise
     */
    public boolean craftBucket() {
        if (getCount(ItemType.WATER) >= 3) {
            for (int i = 0; i < 3; i++) {
                remove(ItemType.WATER);
            }
            items.put(ItemType.BUCKET, items.getOrDefault(ItemType.BUCKET, 0) + 1);
            return true;
        }
        return false;
    }

    /**
     * Returns a message describing the effect of using a particular item.
     *
     * @param type the item type that was used
     * @return a string describing the item's effect
     */
    public String getItemMessage(ItemType type) {
        return switch (type) {
            case BANDAGE -> "Used: bandages (+50 HP)";
            case WATER -> "Used: water (+50 STAMINA)";
            case ARMOR -> "Used: armor (+50 ARMOR)";
            case BOXER -> "Equipped: knuckle";
            case BUCKET -> "Used: bucket (+25 HP & STAMINA BOOST FOR 5 SECS)";
        };
    }
}
