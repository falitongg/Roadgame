package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;

public class Player extends Entity {
    private float stamina;// Energy for running and
    private float thirst;// Increases over time, affects
    private float hunger;// Like starvation, but grows faster, also harmful
    private float armor;// Defense — reduces the damage received
    private float radiation; // Radiation level — causes damage over time
    private float speed;//Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).
    private final Inventory inventory;

    public Player(float x, float y, float health, float damage, Inventory inventory, float speed, float radiation, float armor, float hunger, float thirst, float stamina) {
        super(x, y, health, damage);
        this.inventory = inventory;
        this.speed = speed;
        this.radiation = radiation;
        this.armor = armor;
        this.hunger = hunger;
        this.thirst = thirst;
        this.stamina = stamina;
    }
    public Player() {
        this(0, 0, 100, 10, new Inventory(), 1.0f, 0, 10, 0, 0, 100);
    }


    //move function
    public void move(Direction direction) {
        //TODO реализовать перемещение игрока
    }
    //sprint function
    public void sprint(Direction direction) {
        //TODO реализовать бег игрока
    }
    //permadeath function
    public void die() {
        //TODO реализовать смерть игрока как конец игры
    }
    public void useItemFromInventory(int index) {
        Item item = inventory.getItem(index);
        if (item != null) {
            useItem(item);
        }
    }

    //use item function
    public void useItem(Item item) {
        //TODO реализовать функцию использования предмета
    }


}
