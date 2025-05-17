package cz.cvut.fel.pjv.sokolant.roughlikegame;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.LoggingUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        showMainMenu(stage);
    }

    @Override
    public void init() {
        LoggingUtil.setup();
    }

    public static void showMainMenu(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/cz/cvut/fel/pjv/sokolant/roughlikegame/designtest.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setTitle("THE ROAD");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
