package cz.cvut.fel.pjv.sokolant.roughlikegame.model;


import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;


public class Trader extends Entity implements EntityDrawable, Interactable {
    private static final float TRADER_WIDTH  = 48;
    private static final float TRADER_HEIGHT = 96;
    private static final double INTERACT_DIST = 60;


    private final Image idleSprite = new Image(getClass().getResourceAsStream("/images/npcs/trader_idle_r.png"));
    private final Image tradeSprite = new Image(getClass().getResourceAsStream("/images/npcs/trader_trade_r.png"));

    // товары (можно сделать new ArrayList, если нужно удалять покупку)
    private final List<Item> items = List.of(
            new Item("Bandage", ItemType.BANDAGE, 50),
            new Item("Water", ItemType.WATER, 30),
            new Item("Armor", ItemType.ARMOR, 120),
            new Item("Boxer", ItemType.BOXER, 300),
            new Item("Key-card", ItemType.KEYCARD, 600)
    );

    /* ----------- КОНСТРУКТОР ----------- */
    public Trader(float x, float y) {
        // у торговца здоровье = бесконечность и урон = 0
        super(x, y, Float.MAX_VALUE, 0);
        this.width  = TRADER_WIDTH;
        this.height = TRADER_HEIGHT;
    }

    /* ----------- ГЕТТЕР ----------- */
    public List<Item> getItems() {
        return items;
    }

    /* ----------- ЛОГИКА ПОКУПКИ ----------- */
    public boolean buy(Player player, int index) {
        if (index < 0 || index >= items.size()) return false;
        Item item = items.get(index);

        if (player.spendMoney(item.getPrice())) {
            player.addItemToInventory(item);
            // если товар конечный:
            // items.remove(index);
            return true;
        }
        return false;
    }

    /* ----------- EntityDrawable ----------- */
    @Override
    public double getRenderY() {
        return getY();
    }

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

        gc.drawImage(toDraw, getX() - cameraX, getRenderY()+TRADER_HEIGHT);
    }

    /* ----------- Interactable ----------- */
    @Override
    public void interact(Player player) {
        System.out.println("-- Trader --");
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.printf("[%d] %s ..... %d $\n", i, it.getName(), it.getPrice());
        }
        System.out.println("Press 0–5 to buy, Esc to leave");

        // можно выставить флаг GameState.TRADE и обрабатывать ввод в InputHandler
    }

    public Rectangle2D getBounds() {                     // для коллизии
        return new Rectangle2D(getX(), getY() - TRADER_HEIGHT, TRADER_WIDTH, TRADER_HEIGHT);
    }

}
