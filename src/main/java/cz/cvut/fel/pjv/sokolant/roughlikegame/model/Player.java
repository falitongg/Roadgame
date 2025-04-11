package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public class Player {
    private int health;     // Player's total health, dies at 0
    private int stamina;    // Energy for running and fighting
    private int thirst;     // Increases over time, affects health
    private int hunger;     // Like starvation, but grows faster, also harmful
    private int armor;      // Defense — reduces the damage received
    private int radiation;  // Radiation level — causes damage over time
    private int speed;      //Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).
//SETTERS
    public void setHealth(int health) {
        this.health = health;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setRadiation(int radiation) {
        this.radiation = radiation;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
//GETTERS
    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public int getThirst() {
        return thirst;
    }

    public int getHunger() {
        return hunger;
    }

    public int getRadiation() {
        return radiation;
    }

    public int getArmor() {
        return armor;
    }

    public int getSpeed() {
        return speed;
    }
//CONSTRUCTOR
    public Player(int health, int stamina, int thirst, int armor, int hunger, int radiation, int speed) {
        this.health = health;
        this.stamina = stamina;
        this.thirst = thirst;
        this.armor = armor;
        this.hunger = hunger;
        this.radiation = radiation;
        this.speed = speed;
    }
}
