package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<ItemType, Integer> items;
    
    private static final Map<ItemType, Image> itemsIcons = Map.of(
            ItemType.BANDAGE, new Image(Inventory.class.getResourceAsStream("/images/items/bandage.png")),
            ItemType.WATER, new Image(Inventory.class.getResourceAsStream("/images/items/water.png")),
            ItemType.ARMOR, new Image(Inventory.class.getResourceAsStream("/images/items/armor.png")),
            ItemType.BOXER, new Image(Inventory.class.getResourceAsStream("/images/items/boxer.png")),
            ItemType.BUCKET, new Image(Inventory.class.getResourceAsStream("/images/items/water_bucket.png"))
    );

    public static Image getIcon(ItemType type) {
        if (type == null) return null;
        return itemsIcons.get(type);
    }


    public Inventory() {
        this.items = new HashMap<>();
    }

    public void add(Item item) {
        ItemType type = item.getType();
        if (type == ItemType.BOXER && hasItem(ItemType.BOXER)) {
            return;
        }
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

    public boolean craftBucket(){
        if(getCount(ItemType.WATER) >= 3){
            for(int i = 0; i < 3; i++){
                remove(ItemType.WATER);
            }
            items.put(ItemType.BUCKET, items.getOrDefault(ItemType.BUCKET, 0) + 1);
            return true;
        }
        return false;
    }
    public String getItemMessage(ItemType type) {
        return switch (type){
            case BANDAGE -> "Used: bandages (+50 HP)";
            case WATER -> "Used: water (+50 STAMINA)";
            case ARMOR -> "Used: armor (+50 ARMOR)";
            case BOXER -> "Equipped: knuckle";
            case BUCKET -> "Used: bucket (+25 HP & STAMINA BOOST FOR 5 SECS)";
        };
    }
}
