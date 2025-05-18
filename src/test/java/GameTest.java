import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Enemy;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Player;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        //each test starts with a ‘clean’ new game
        game = new Game();
    }

    /** 1. After construction: empty lists and MENU-state */
    @Test
    void testInitialState() {
        assertEquals(GameState.MENU, game.getState(),    "Game should be in MENU");
        assertTrue(game.getEnemies().isEmpty(),          "The enemy list is empty");
        assertTrue(game.getObstacles().isEmpty(),        "The obstacle list is empty");
        assertTrue(game.getItems().isEmpty(),            "The list of items is empty");
        assertTrue(game.getTraders().isEmpty(),          "The list of traders is empty");
        assertNull(game.getCurrentTrader(),              "currentTrader === null");
    }

    /** 2. startGame and endGame change state correctly */
    @Test
    void testStartAndEndGame() {
        game.startGame();
        assertEquals(GameState.PLAYING, game.getState());

        game.endGame();
        assertEquals(GameState.GAME_OVER, game.getState());
    }

    /** 3. spawnEnemy actually adds the enemy to the list */
    @Test
    void testSpawnMockEnemyAddsToList() {
        // Arrange
        Enemy enemy = new Enemy(0, 0, 10, 15, 1, 100) {
            @Override
            public void update(Player player) {

            }

            @Override
            public void render(GraphicsContext gc, double cameraX, Player player) {

            }
        };

        // Act
        game.spawnEnemy(enemy);

        // Assert
        assertEquals(1, game.getEnemies().size());
        assertSame(enemy, game.getEnemies().get(0));
    }


}
