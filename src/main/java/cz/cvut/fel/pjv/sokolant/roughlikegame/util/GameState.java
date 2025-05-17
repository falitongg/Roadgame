package cz.cvut.fel.pjv.sokolant.roughlikegame.util;

/**
 * Represents the current state of the game.
 * Used to control game logic flow and UI behavior.
 */
public enum GameState {

    /** The game is displaying the main menu. */
    MENU,

    /** The game is actively running and updating. */
    PLAYING,

    /** The game is paused (e.g., via ESC menu). */
    PAUSED,

    /** The player has died and the game is over. */
    GAME_OVER,

    /** The player is currently trading with a trader. */
    TRADE
}
