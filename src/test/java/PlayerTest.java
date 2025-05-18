import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Inventory;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Player;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        //create a spy-object instead of a pure Player
        // by default Player() health=100, damage=33, armor=100, stamina=100
        player = new Player(
                0f,
                0f,
                100f,
                33f,
                new Inventory(),
                1.0f,
                100f,
                100f,
                0){
            @Override
            public void flash() {
                // nothing
            }
        };

        // creates an ‘empty’ Game, which is not in PLAYING state
        Game dummy = new Game();
        dummy.setState(GameState.MENU);

        // slip it into the player - now game != null, and game.getState() is safe
        player.setGame(dummy);
    }

    /** 1. restoreHealth must not exceed maxHealth */
    @Test
    void testRestoreHealthCapsMax(){
        player.setMaxHealth(100f);
        player.setHealth(90f);
        player.restoreHealth(20f);
        assertEquals(100f, player.getHealth(), "HP must not exceed maxHealth");
    }

    /** 2. RestoreStamina must not exceed 100 */
    @Test
    void testRestoreStaminaCapsAt100() {
        player.setStamina(90f);
        player.restoreStamina(20f);
        assertEquals(100f, player.getStamina(), "Stamina must not exceed 100");
    }

    /** 3. spendStamina must not go negative */
    @Test
    void testSpendStaminaFloorsAtZero() {
        player.setStamina(10f);
        player.spendStamina(20f);
        assertEquals(0f, player.getStamina(), "Stamina can't be below zero");
    }

    /** 4. takeDamage without armour reduces health by full value */
    @Test
    void testTakeDamageWithoutArmor(){
        player.setArmor(0f);
        player.takeDamage(20);
        assertEquals(80f, player.getHealth(), "Health should decrease by 20");
        assertEquals(0f, player.getArmor(), "Armor can't be below zero");
    }

    /** 5. takeDamage when player is blocking reduces health and armour by 30% of damage */
    @Test
    void testTakeDamageWithArmor(){
        player.setBlocking(true);
        float playersFullHealth = player.getHealth();
        float playersFullArmor = player.getArmor();
        player.takeDamage(20);
        assertEquals(playersFullHealth - 6f, player.getHealth(), "HP should decrease by 20*0.3=6.");
        assertEquals(playersFullArmor - 6f, player.getArmor(), "Armor should decrease by 20*0.3=6.");
    }

    /** 6. equipKnuckle switches knuckle and changes damage */
    @Test
    void testEquipKnuckleTogglesDamage() {
        assertFalse(player.hasKnuckleEquipped());
        assertEquals(33f, player.getDamage());
        player.equipKnuckle();
        assertTrue(player.hasKnuckleEquipped());
        assertEquals(50f, player.getDamage());
        player.equipKnuckle();
        assertFalse(player.hasKnuckleEquipped());
        assertEquals(33f, player.getDamage());
    }

    /** 7. spendMoney returns true/false and changes money correctly */
    @Test
    void testSpendMoneyLogic() {
        player.setMoney(10);
        assertFalse(player.spendMoney(20), "Can't spend more than player's money");
        assertEquals(10, player.getMoney());
        player.addMoney(50);
        assertTrue(player.spendMoney(20), "Now it works");
        assertEquals(40, player.getMoney());
    }
}
