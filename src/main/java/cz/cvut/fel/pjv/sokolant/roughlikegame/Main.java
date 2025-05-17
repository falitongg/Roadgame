package cz.cvut.fel.pjv.sokolant.roughlikegame;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.LoggingUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.*;

/**
 * Main class for launching the Roughlike Game application.
 * Initializes and displays the main menu.
 */
public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void init() {
        logger.info("Initializing application");
        LoggingUtil.setup();
    }

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        showMainMenu(stage);
    }

    /**
     * Loads and displays the main menu scene from an FXML file.
     * Handles IOException if FXML loading fails.
     *
     * @param stage The primary stage provided by JavaFX
     */
    public static void showMainMenu(Stage stage) {
        try {
            logger.info("Loading FXML for main menu: designtest.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Main.class.getResource("/cz/cvut/fel/pjv/sokolant/roughlikegame/designtest.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setTitle("THE ROAD");
            stage.setScene(scene);
            stage.show();
            logger.info("Main menu displayed");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load main menu FXML", e);
        }
    }

    /**
     * Entry point of the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        logger.info("Application launch invoked");
        launch(args);
    }
}