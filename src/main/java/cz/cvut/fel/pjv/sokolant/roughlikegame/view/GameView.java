package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

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

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private Image background;
    private Image player;

    private double playerX = 100;
    private double playerY = HEIGHT - 200;

    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game View");

        Pane pane = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        pane.getChildren().add(canvas);

        primaryStage.setScene(new Scene(pane, WIDTH, HEIGHT));
//        primaryStage.initStyle(StageStyle.UNDECORATED);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image background = new Image(getClass().getResourceAsStream("/images/bgs/background_alpha.png"));
        Image player = new Image(getClass().getResourceAsStream("/images/player/player1.png"));

        playerX = 100;
        playerY = HEIGHT - 200; // гарантированно не обрежет игрока

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, WIDTH, HEIGHT); // очищаем всё
                gc.drawImage(background, 0, 0, WIDTH, HEIGHT); // фон
                gc.drawImage(player ,playerX, playerY, 200, 200); // игрок
            }
        }.start();

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
