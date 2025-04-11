package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public class Player {
    private int health;     // Player's total health, dies at 0
    private int stamina;    // Energy for running and fighting
    private int thirst;     // Increases over time, affects health
    private int hunger;     // Like starvation, but grows faster, also harmful
    private int armor;      // Gets from surroundings/enemies, deals damage
    private int radiation;  // Defense - reduces the damage received
    private int speed;      //Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).

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
}
