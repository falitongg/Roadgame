package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameView extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game View");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
