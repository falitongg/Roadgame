package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateLoader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateSaver;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class InputHandler {
    private final Game game;
    public InputHandler(Game game) {
        this.game = game;
    }
    public void handleInput(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W -> game.getPlayer().setMovingUp(true);
            case S -> game.getPlayer().setMovingDown(true);
            case A -> game.getPlayer().setMovingLeft(true);
            case D -> game.getPlayer().setMovingRight(true);
            case SPACE -> game.getPlayer().jump();
            case SHIFT -> game.getPlayer().setSprinting(true);
            case CONTROL -> game.getPlayer().setCrouching(true);
            case F5 -> GameStateSaver.saveGame(game, "save.json");
            case F9 -> GameStateLoader.loadGame(game, "save.json");

        }
    }

    public void handleKeyReleased(KeyEvent event) {
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



}
