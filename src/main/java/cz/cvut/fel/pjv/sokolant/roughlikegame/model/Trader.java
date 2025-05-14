package cz.cvut.fel.pjv.sokolant.roughlikegame.model;


import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Trader extends Entity implements EntityDrawable, Interactable {

    private static final float TRADER_WIDTH  = 48;
    private static final float TRADER_HEIGHT = 96;

    private final Image sprite = new Image(
            getClass().getResourceAsStream("/images/npcs/trader_idle_r.png")
    );

    // товары (можно сделать new ArrayList, если нужно удалять покупку)
    private final List<Item> items = List.of(
            new Bandage(), new WaterBottle()
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
        return getY()+TRADER_HEIGHT;
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        gc.drawImage(sprite, getX() - cameraX, getRenderY());
    }

    /* ----------- Interactable ----------- */
    @Override
    public void interact(Player player) {
        // пока консольный вывод; позже заменишь на JavaFX окно
        System.out.println("-- Trader --");
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.printf("[%d] %s ..... %d $\n", i, it.getName(), it.getPrice());
        }
        System.out.println("Press 0/1 to buy, Esc to leave");

        // можно выставить флаг GameState.TRADE и обрабатывать ввод в InputHandler
    }

    public Rectangle2D getBounds() {                     // для коллизии
        return new Rectangle2D(getX(), getY() - TRADER_HEIGHT, TRADER_WIDTH, TRADER_HEIGHT);
    }

}
