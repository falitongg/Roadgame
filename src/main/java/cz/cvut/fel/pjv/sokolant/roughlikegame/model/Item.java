package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;

public class Item {
    private String name;
    private ItemType type;  //Category from ItemType
    public int price;
    public Item(String name, ItemType type, int price) {
        this.name = name;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

}
