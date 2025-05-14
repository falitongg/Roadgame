package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<ItemType, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void add(Item item) {
        ItemType type = item.getType();
        items.put(type, items.getOrDefault(type, 0) + 1);
    }

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

    public int getCount(ItemType type) {
        return items.getOrDefault(type, 0);
    }

    public boolean hasItem(ItemType type) {
        return items.getOrDefault(type, 0) > 0;
    }

    public Map<ItemType, Integer> getItems() {
        return items;
    }
}
