package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateLoader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateSaver;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Trader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

/**
 * Handles keyboard and mouse input for the game.
 * Processes all player actions depending on the game state.
 */
public class InputHandler {
    private final Game game;
    private final Camera camera;
    private final GameView gameView;
    private static final Logger logger = Logger.getLogger(InputHandler.class.getName());

    /**
     * Constructs a new InputHandler for the given game, camera, and game view.
     *
     * @param game the game model
     * @param camera the camera used for the view
     * @param gameView the main game view
     */
    public InputHandler(Game game, Camera camera, GameView gameView) {
        this.game = game;
        this.camera = camera;
        this.gameView = gameView;
    }

    /**
     * Handles key press events depending on the current game state.
     *
     * @param keyEvent the keyboard event
     */
    public void handleInput(KeyEvent keyEvent) {
        GameState state = game.getState();
        logger.info("Key pressed: " + keyEvent.getCode());

        if (state == GameState.PLAYING) {
            // Handle main gameplay controls
            switch (keyEvent.getCode()) {
                case W -> game.getPlayer().setMovingUp(true);
                case S -> game.getPlayer().setMovingDown(true);
                case A -> game.getPlayer().setMovingLeft(true);
                case D -> game.getPlayer().setMovingRight(true);
                case SPACE -> game.getPlayer().jump();
                case SHIFT -> game.getPlayer().setSprinting(true);
                case CONTROL -> game.getPlayer().setCrouching(true);

                case DIGIT1 -> {
                    String message = game.getPlayer().useItem(ItemType.BANDAGE);
                    gameView.showNotificatrion(message);
                }
                case DIGIT2 -> {
                    String message = game.getPlayer().useItem(ItemType.WATER);
                    gameView.showNotificatrion(message);
                }
                case DIGIT3 -> {
                    String message = game.getPlayer().useItem(ItemType.ARMOR);
                    gameView.showNotificatrion(message);
                }
                case DIGIT4 -> {
                    if (game.getPlayer().getInventory().hasItem(ItemType.BOXER)) {
                        if (!game.getPlayer().hasKnuckleEquipped()) {
                            String message = game.getPlayer().useItem(ItemType.BOXER);
                            gameView.showNotificatrion(message);
                        } else if (game.getPlayer().hasKnuckleEquipped()) {
                            game.getPlayer().useItem(ItemType.BOXER);
                            String message = "You took off the knuckles.";
                            gameView.showNotificatrion(message);
                        }
                    } else {
                        String message = "No item found";
                        gameView.showNotificatrion(message);
                    }
                }
                case DIGIT5 -> {
                    String message = game.getPlayer().useItem(ItemType.BUCKET);
                    gameView.showNotificatrion(message);
                }
                case C -> {
                    if (game.getPlayer().getInventory().craftBucket()) {
                        String message = "Bucket have been crafted";
                        gameView.showNotificatrion(message);
                    } else {
                        String message = "You don't have 3 water bottles to craft the bucket";
                        gameView.showNotificatrion(message);
                    }
                }
                case F5 -> GameStateSaver.saveGame(game, "saves/save_" + System.currentTimeMillis() + ".json", camera.getX());
                case F9 -> {
                    String latest = GameStateLoader.findLatestSaveFile("saves/");
                    if (latest != null) {
                        GameStateLoader.loadGame(game, latest, camera);
                    } else {
                        System.out.println("ERROR");
                    }
                    gameView.resetAfterLoad();
                }
                case E -> {
                    Trader tr = findNearbyTrader();
                    if (tr != null) {
                        game.setCurrentTrader(tr);
                        game.setState(GameState.TRADE);
                    } else {
                        game.getPlayer().pickUpItem();
                    }
                }
                case ESCAPE -> game.setState(GameState.MENU);
            }
        } else if (state == GameState.MENU) {
            switch (keyEvent.getCode()) {
                case N -> game.setState(GameState.PLAYING);
                case Y -> gameView.setTransitionScheduled(true);
            }
        } else if (state == GameState.TRADE) {
            switch (keyEvent.getCode()) {
                case DIGIT1 -> {
                    if (game.getCurrentTrader().buy(game.getPlayer(), 0)) {
                        String message = "+1 BANDAGE";
                        sendMessage(message);
                    } else {
                        String message = "Not enough money";
                        sendMessage(message);
                    }
                }
                case DIGIT2 -> {
                    if (game.getCurrentTrader().buy(game.getPlayer(), 1)) {
                        String message = "+1 WATER";
                        sendMessage(message);
                    } else {
                        String message = "Not enough money";
                        sendMessage(message);
                    }
                }
                case DIGIT3 -> {
                    if (game.getCurrentTrader().buy(game.getPlayer(), 2)) {
                        String message = "+1 ARMOR";
                        sendMessage(message);
                    } else {
                        String message = "Not enough money";
                        sendMessage(message);
                    }
                }
                case DIGIT4 -> {
                    if (game.getPlayer().getInventory().hasItem(ItemType.BOXER)) {
                        String message = "You don't need another one";
                        gameView.showNotificatrion(message);
                    } else {
                        if (game.getCurrentTrader().buy(game.getPlayer(), 3)) {
                            String message = "+1 BOXER";
                            sendMessage(message);
                        } else {
                            String message = "Not enough money";
                            sendMessage(message);
                        }
                    }
                }
                case ESCAPE -> {
                    exitTrade();
                    game.getPlayer().resetMovement();
                    GameStateSaver.saveGame(game, "saves/save_" + System.currentTimeMillis() + ".json", camera.getX());
                }
            }
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (game.getState() != GameState.PLAYING) return;

        logger.info("Key released: " + event.getCode());

        switch (event.getCode()) {
            case W -> game.getPlayer().setMovingUp(false);
            case S -> game.getPlayer().setMovingDown(false);
            case A -> game.getPlayer().setMovingLeft(false);
            case D -> game.getPlayer().setMovingRight(false);
            case SHIFT -> game.getPlayer().setSprinting(false);
            case CONTROL -> game.getPlayer().setCrouching(false);
        }
    }

    public void handleMousePressed(MouseEvent event) {
        logger.info("Mouse pressed: " + event.getButton());

        if (event.getButton() == MouseButton.PRIMARY) {
            game.getPlayer().attack(game.getEnemies());
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            game.getPlayer().setBlocking(true);
        }
    }

    public void handleMouseReleased(MouseEvent event) {
        logger.info("Mouse released: " + event.getButton());

        if (event.getButton() == MouseButton.SECONDARY) {
            game.getPlayer().setBlocking(false);
        }
    }

    private Trader findNearbyTrader() {
        for (Trader t : game.getTraders()) {
            if (t.getBounds().intersects(game.getPlayer().getBounds())) {
                return t;
            }
        }
        return null;
    }

    private void exitTrade() {
        game.setCurrentTrader(null);
        game.setState(GameState.PLAYING);
    }

    public void sendMessage(String message) {
        gameView.showNotificatrion(message);
    }
}
