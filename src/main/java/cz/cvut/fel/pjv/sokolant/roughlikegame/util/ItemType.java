package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Item;
import javafx.scene.image.Image;

public enum ItemType {
    BANDAGE,
    WATER,
    ARMOR,
    KEYCARD,
    BOXER;

    private static final ItemType[] spawnable = {
            BANDAGE, WATER, ARMOR
    };

    public static ItemType getRandom(){
        return spawnable[(int)(Math.random() * spawnable.length)];
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
