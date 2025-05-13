package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }
    public void addItem(Item item) {
        items.add(item);
    }
    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public int getSize() {
        return items.size();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(Item item) {
        items.add(item);
    }
}
