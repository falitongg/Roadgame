package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private Button newGameButton;
    @FXML
    private void newGame() {
        GameView gameView = new GameView();
        try{
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            gameView.start(stage);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
