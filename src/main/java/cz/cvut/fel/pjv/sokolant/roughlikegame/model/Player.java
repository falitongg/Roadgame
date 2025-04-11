package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

public class Player {
    private int health;     // Player's total health, dies at 0
    private int stamina;    // Energy for running and fighting
    private int thirst;     // Increases over time, affects health
    private int hunger;     // Like starvation, but grows faster, also harmful
    private int armor;      // Gets from surroundings/enemies, deals damage
    private int radiation;  // Defense - reduces the damage received
    private int speed;      //Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).

}
