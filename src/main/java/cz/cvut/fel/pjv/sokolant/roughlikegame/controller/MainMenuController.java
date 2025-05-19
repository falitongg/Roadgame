package cz.cvut.fel.pjv.sokolant.roughlikegame.controller;

import cz.cvut.fel.pjv.sokolant.roughlikegame.Main;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateLoader;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the main menu screen.
 * Handles starting a new game, loading saved games, toggling logging, and quitting the application.
 */
public class MainMenuController {
    /** Logger for main menu events. */
    private static final Logger logger = Logger.getLogger(MainMenuController.class.getName());

    /** Button to start a new game. */
    @FXML
    private Button newGameButton;

    /** Button to load an existing saved game. */
    @FXML
    private Button loadGameButton;

    /** Button to toggle logging on or off. */
    @FXML
    private Button loggerButton;

    /** Button to quit the application. */
    @FXML
    private Button quitButton;

    /** Dropdown list of available save files. */
    @FXML
    private ComboBox<String> savesComboBox;

    /**
     * Initialize the controller after the FXML elements are loaded.
     * Populates the save file list and sets the logger button text based on current logging state.
     */
    @FXML
    public void initialize() {
        logger.info("Initializing MainMenuController: loading save files into ComboBox");
        File saveFolder = new File("saves");
        if (saveFolder.exists()) {
            // List all JSON files in the saves directory
            File[] saves = saveFolder.listFiles((dir, name) -> name.endsWith(".json"));
            if (saves != null) {
                for (File save : saves) {
                    savesComboBox.getItems().add(save.getName());
                    logger.fine("Found save file: " + save.getName());
                }
            }
        } else {
            logger.warning("Save folder not found: 'saves' directory does not exist");
        }
        // Select the first save by default, if any exist
        if (!savesComboBox.getItems().isEmpty()) {
            savesComboBox.getSelectionModel().selectFirst();
            logger.info("Default save selected: " + savesComboBox.getValue());
        }
        // Initialize logger toggle button text based on root logger level
        boolean loggingEnabled = Logger.getLogger(" ").getLevel() != Level.OFF;
        loggerButton.setText(loggingEnabled ? "Disable Logging" : "Enable Logging");
    }

    /**
     * Toggles logging on or off when the logger button is clicked.
     * Adjusts the root logger level and updates the button text.
     */
    @FXML
    private void toggleLogging() {
        Logger root = Logger.getLogger("");
        if (root.getLevel() == Level.OFF) {
            root.setLevel(Level.INFO);
            logger.info("Logging enabled via UI");
            loggerButton.setText("Disable Logging");
        } else {
            logger.info("Logging disabled via UI");
            loggerButton.setText("Enable Logging");
            root.setLevel(Level.OFF);
        }
    }

    /**
     * Starts a new game when the New Game button is clicked.
     * Sets up a new GameView and transitions to the gameplay scene.
     */
    @FXML
    private void newGame() {
        logger.info("New Game button clicked");
        GameView gameView = new GameView();
        Stage stage = (Stage) newGameButton.getScene().getWindow();
        gameView.setReturnToMenuCallback(() -> Main.showMainMenu(stage));
        gameView.start(stage);
        logger.info("New game view started");
    }

    /**
     * Loads the selected saved game when the Load Game button is clicked.
     * Reads the game state from file, initializes the model and view, and transitions scenes.
     */
    @FXML
    private void loadGame() {
        String saveFile = savesComboBox.getValue();
        logger.info("Load Game button clicked, selected save: " + saveFile);
        if (saveFile == null || saveFile.isEmpty()) {
            logger.warning("No save selected, aborting load");
            return;
        }
        // Load game state from JSON file
        Game game = new Game();
        Camera camera = new Camera(640);
        try {
            GameStateLoader.loadGame(game, "saves/" + saveFile, camera);
            logger.info("Game state loaded from file: " + saveFile);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading game state from file: " + saveFile, e);
            return;
        }
        // Prepare the view for the loaded game
        GameView view = new GameView(game, camera);
        view.resetAfterLoad();
        logger.info("Game view prepared after load");
        // Transition to the game scene
        Stage stage = (Stage) loadGameButton.getScene().getWindow();
        view.setReturnToMenuCallback(() -> Main.showMainMenu(stage));
        view.start(stage);
        logger.info("Loaded game view displayed");
    }

    /**
     * Quits the application when the Quit button is clicked.
     * Closes the primary stage window.
     */
    @FXML
    private void quitGame() {
        logger.info("Quit Game button clicked -- closing application");
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
}
