package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.Main;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button newGameButton;
    @FXML
    private Button quitButton;
    @FXML
    private void newGame() {
        GameView gameView = new GameView();
        try {
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            gameView.setReturnToMenuCallback(() -> Main.showMainMenu(stage));
            gameView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void quitGame() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
}
