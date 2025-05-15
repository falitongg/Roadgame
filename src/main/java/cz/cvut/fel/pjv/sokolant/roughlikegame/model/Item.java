package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Item implements EntityDrawable {
    private String name;
    private ItemType type;  //Category from ItemType
    public int price;
    private Image image;
    private float x, y;

    public Item(String name, ItemType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.image = ItemType.getImage(type);
        this.x = 0;
        this.y = 0;
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

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getRenderY() {
        return y-150;
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        if (image == null) return;
        gc.drawImage(image, x - cameraX, y);
    }
}
