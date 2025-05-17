package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Represents a trader NPC in the game world.
 * The trader offers items for sale and interacts with the player during trade sessions.
 * Implements {@link EntityDrawable} for rendering and {@link Interactable} for in-world interaction.
 */
public class Trader extends Entity implements EntityDrawable, Interactable {

    private static final float TRADER_WIDTH  = 48;
    private static final float TRADER_HEIGHT = 96;
    private static final double INTERACT_DIST = 60;

    private final Image idleSprite = new Image(getClass().getResourceAsStream("/images/npcs/trader_idle_r.png"));
    private final Image tradeSprite = new Image(getClass().getResourceAsStream("/images/npcs/trader_trade_r.png"));

    private final List<Item> items = List.of(
            new Item("Bandage", ItemType.BANDAGE, 50),
            new Item("Water", ItemType.WATER, 30),
            new Item("Armor", ItemType.ARMOR, 120),
            new Item("Boxer", ItemType.BOXER, 300)
    );

    /**
     * Constructs a Trader at a given position.
     *
     * @param x the X coordinate of the trader
     * @param y the Y coordinate of the trader
     */
    public Trader(float x, float y) {
        super(x, y, Float.MAX_VALUE, 0);
        this.width  = TRADER_WIDTH;
        this.height = TRADER_HEIGHT;
    }

    /**
     * Returns the list of items the trader offers.
     *
     * @return immutable list of items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Attempts to sell an item to the player.
     *
     * @param player the player who wants to buy the item
     * @param index the index of the item in the list
     * @return true if the purchase was successful, false otherwise
     */
    public boolean buy(Player player, int index) {
        if (index < 0 || index >= items.size()) return false;
        Item item = items.get(index);

        if (item.getType() == ItemType.BOXER && player.getInventory().hasItem(ItemType.BOXER)) {
            return false;
        }

        if (player.spendMoney(item.getPrice())) {
            player.addItemToInventory(item);
            return true;
        }

        return false;
    }

    /**
     * Returns the Y coordinate used for rendering depth sorting.
     *
     * @return Y position for depth rendering
     */
    @Override
    public double getRenderY() {
        return getY();
    }

    /**
     * Renders the trader on screen. Displays a different sprite when the player is nearby or trading.
     *
     * @param gc graphics context
     * @param cameraX camera offset
     * @param player reference to the player
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        Image toDraw = idleSprite;

        boolean near = Math.abs(player.getX() - getX()) < INTERACT_DIST
                && Math.abs(player.getY() - getY()) < INTERACT_DIST;

        boolean trading = player.getGame().getState() == GameState.TRADE
                && player.getGame().getCurrentTrader() == this;

        if (near || trading) {
            toDraw = tradeSprite;
        }

        gc.drawImage(toDraw, getX() - cameraX, getRenderY() + TRADER_HEIGHT);
    }

    /**
     * Returns a rectangle representing the trader's collision bounds.
     *
     * @return collision bounds
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY() - TRADER_HEIGHT, TRADER_WIDTH, TRADER_HEIGHT);
    }
}