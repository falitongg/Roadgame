package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

/**
 * Represents an entity in the game world that can be interacted with by the player.
 * Examples include traders, doors, terminals, etc.
 */
public interface Interactable {

    /**
     * Performs an interaction with the player.
     * The specific behavior depends on the implementing class.
     *
     * @param player the player who initiates the interaction
     */
    void interact(Player player);
}
