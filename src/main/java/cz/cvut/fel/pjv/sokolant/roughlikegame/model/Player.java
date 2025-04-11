package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public class Player {
    private float health;// Player's total health, dies at 0
    private float stamina;// Energy for running and
    private float thirst;// Increases over time, affects
    private float hunger;// Like starvation, but grows faster, also harmful
    private float armor;// Defense — reduces the damage received
    private float radiation; // Radiation level — causes damage over time
    private float speed;//Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).
    private float x;
    private float y;

    // === SETTERS ===
    public void setHealth(float health) {
        this.health = health;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void setThirst(float thirst) {
        this.thirst = thirst;
    }

    public void setHunger(float hunger) {
        this.hunger = hunger;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public void setRadiation(float radiation) {
        this.radiation = radiation;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    // === GETTERS ===
    public float getHealth() {
        return health;
    }

    public float getStamina() {
        return stamina;
    }

    public float getThirst() {
        return thirst;
    }

    public float getHunger() {
        return hunger;
    }

    public float getRadiation() {
        return radiation;
    }

    public float getArmor() {
        return armor;
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // === CONSTRUCTOR ===
    public Player(float health, float armor, float stamina, float thirst, float hunger, float radiation, float speed) {
        this.health = health;
        this.armor = armor;
        this.stamina = stamina;
        this.thirst = thirst;
        this.hunger = hunger;
        this.radiation = radiation;
        this.speed = speed;
        this.x = 0;
        this.y = 0;
    }
}
