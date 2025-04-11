package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;

public class Item {
    private String itemName;
    private String itemDescription;
    private ItemType itemType;  //Category from ItemType
    private float effectValue;

    public Item(String itemName, String itemDescription, ItemType itemType, float effectValue) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.effectValue = effectValue;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public float getEffectValue() {
        return effectValue;
    }
}
