package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Item;
import javafx.scene.image.Image;

public enum ItemType {
    BANDAGE,
    WATER,
    ARMOR,
    KEYCARD,
    BOXER;

    public static ItemType getRandom(){
        ItemType[] values = ItemType.values();
        return values[(int)(Math.random()*values.length)];
    }

    public static Image getImage(ItemType type) {
        switch (type) {
            case BANDAGE: return new Image(Item.class.getResourceAsStream("/images/items/bandage.png"));
            case WATER: return new Image(Item.class.getResourceAsStream("/images/items/water.png"));
            case ARMOR: return new Image(Item.class.getResourceAsStream("/images/items/armor.png"));
            default: return null;
        }
    }
}
