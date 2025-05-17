package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a collectible or usable item in the game world.
 * Implements rendering and provides properties such as name, type, price, and position.
 */
public class Item implements EntityDrawable {

    private String name;
    private ItemType type;  // Category from ItemType
    public int price;
    private Image image;
    private float x, y;

    /**
     * Constructs an item with a given name, type, and price.
     * The item's image is retrieved from the ItemType.
     *
     * @param name  name of the item
     * @param type  type/category of the item
     * @param price purchase price of the item
     */
    public Item(String name, ItemType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.image = ItemType.getImage(type);
        this.x = 0;
        this.y = 0;
    }

    /**
     * Returns the name of the item.
     *
     * @return item name
     */
    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    /**
     * Returns the price of the item.
     *
     * @return item price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the X coordinate of the item.
     *
     * @return item's X position
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the item's position on the map.
     *
     * @param x new X coordinate
     * @param y new Y coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the Y coordinate used for rendering depth sorting.
     *
     * @return Y rendering position
     */
    @Override
    public double getRenderY() {
        return y - 150;
    }

    /**
     * Renders the item at its current position with camera offset.
     *
     * @param gc       graphics context for rendering
     * @param cameraX  current camera X offset
     * @param player   reference to the player (not used in item rendering)
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        if (image == null) return;
        gc.drawImage(image, x - cameraX, y);
    }
}
