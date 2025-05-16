package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.Main;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateLoader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Player;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button newGameButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button settingsButton;

    @FXML
    private ComboBox<String> savesComboBox;

    @FXML
    private GameView newGame() {
        GameView gameView = new GameView();
        try {
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            gameView.setReturnToMenuCallback(() -> Main.showMainMenu(stage));
            gameView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameView;
    }
    @FXML
    public void initialize() {
        File saveFolder = new File("saves");
        if (saveFolder.exists()) {
            File[] saves = saveFolder.listFiles((dir, name) -> name.endsWith(".json"));
            if (saves != null) {
                for (File save : saves) {
                    savesComboBox.getItems().add(save.getName());
                }
            }
        }

        if (!savesComboBox.getItems().isEmpty()) {
            savesComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void loadGame() {
        String save = savesComboBox.getValue();
        if (save == null || save.isEmpty()) return;

        // 1) Модель
        Game game = new Game();
        Camera camera = new Camera(640);
        GameStateLoader.loadGame(game, "saves/" + save, camera);
        game.getPlayer().setCamera(camera);
        game.getPlayer().setGame(game);
        game.startGame();

        // 2) View с загруженным состоянием
        GameView view = new GameView(game, camera);
        view.resetAfterLoad();

        // 3) Запуск
        try {
            Stage stage = (Stage) loadGameButton.getScene().getWindow();
            view.setReturnToMenuCallback(() -> Main.showMainMenu(stage));
            view.start(stage);  // initGame() НЕ вызовется, т.к. alreadyLoaded == true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void settings() {

    }
    @FXML
    private void quitGame() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

}
