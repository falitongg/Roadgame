package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;



public class GameView extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Game game;
    private Image background;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;


    @Override
    public void start(Stage primaryStage) {
        initGame();
        initUI(primaryStage);
        startGameLoop();
    }

    private void initGame() {
        game = new Game();
        background = new Image(getClass().getResourceAsStream("/images/bgs/background_alpha.png"));
    }


    private void initUI(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Roughlike");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        timer.start();
    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        renderBackground();
        renderEntities();
    }
    private void renderBackground() {
        gc.drawImage(background, 0, 0, WIDTH, HEIGHT);
    }
    private void renderEntities() {
        game.getPlayer().render(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }

}

