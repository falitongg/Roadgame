package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateLoader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateSaver;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Trader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class InputHandler {
    private final Game game;
    private final Camera camera;
    private final GameView gameView;

    public InputHandler(Game game, Camera camera, GameView gameView) {
        this.game = game;
        this.camera = camera;
        this.gameView = gameView;
    }
    public void handleInput(KeyEvent keyEvent) {
        GameState state = game.getState();

        if (state == GameState.PLAYING) {
            switch (keyEvent.getCode()) {
                case W -> game.getPlayer().setMovingUp(true);
                case S -> game.getPlayer().setMovingDown(true);
                case A -> game.getPlayer().setMovingLeft(true);
                case D -> game.getPlayer().setMovingRight(true);
                case SPACE -> game.getPlayer().jump();
                case SHIFT -> game.getPlayer().setSprinting(true);
                case CONTROL -> game.getPlayer().setCrouching(true);
                case F5 -> GameStateSaver.saveGame(game, "save.json", camera.getX());
                case F9 -> {
                    GameStateLoader.loadGame(game, "save.json", camera);
                    gameView.resetAfterLoad();
                }
                case E -> {
                    Trader tr = findNearbyTrader();
                    if (tr != null) {
                        game.setCurrentTrader(tr);
                        game.setState(GameState.TRADE);
                    }
                }
            }
        }

        else if (state == GameState.TRADE) {
            switch (keyEvent.getCode()) {
                case DIGIT1 -> game.getCurrentTrader().buy(game.getPlayer(), 0);
                case DIGIT2 -> game.getCurrentTrader().buy(game.getPlayer(), 1);
                case DIGIT3 -> game.getCurrentTrader().buy(game.getPlayer(), 2);
                case DIGIT4 -> game.getCurrentTrader().buy(game.getPlayer(), 3);
                case DIGIT5 -> game.getCurrentTrader().buy(game.getPlayer(), 4);
                case ESCAPE          -> exitTrade();
            }
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (game.getState() != GameState.PLAYING) return;

        switch (event.getCode()) {
            case W -> game.getPlayer().setMovingUp(false);
            case S -> game.getPlayer().setMovingDown(false);
            case A -> game.getPlayer().setMovingLeft(false);
            case D -> game.getPlayer().setMovingRight(false);
            case SHIFT -> game.getPlayer().setSprinting(false);
            case CONTROL -> game.getPlayer().setCrouching(false);
            case DIGIT1 -> game.getPlayer().useItem(ItemType.BANDAGE);
            case DIGIT2 -> game.getPlayer().useItem(ItemType.WATER);
            case DIGIT3 -> game.getPlayer().useItem(ItemType.ARMOR);
            case DIGIT4 -> game.getPlayer().useItem(ItemType.BOXER);
            case DIGIT5 -> game.getPlayer().useItem(ItemType.KEYCARD);

        }
    }

    public void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            game.getPlayer().attack(game.getEnemies());
        }
        if (event.getButton() == MouseButton.SECONDARY) { // ПКМ
            game.getPlayer().setBlocking(true);
        }

    }
    public void handleMouseReleased(MouseEvent event) {
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


}
