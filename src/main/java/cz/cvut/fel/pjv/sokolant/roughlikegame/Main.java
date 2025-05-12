package cz.cvut.fel.pjv.sokolant.roughlikegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showMainMenu(stage);
    }

    public static void showMainMenu(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/cz/cvut/fel/pjv/sokolant/roughlikegame/designtest.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setTitle("ROAD");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
