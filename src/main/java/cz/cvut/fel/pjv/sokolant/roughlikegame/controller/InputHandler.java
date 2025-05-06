package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

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
            case W -> game.getPlayer().move(Direction.UP);
            case S -> game.getPlayer().move(Direction.DOWN);
            case A ->{
                game.getPlayer().setMovingLeft(true);
                game.getPlayer().move(Direction.LEFT);
            }
            case D ->{
                game.getPlayer().setMovingRight(true);
                game.getPlayer().move(Direction.RIGHT);
            }
            case SPACE -> game.getPlayer().jump();
        }
    }
    public void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            game.getPlayer().attack(game.getEnemies());
        }
    }


}
