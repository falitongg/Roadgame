package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class GameView extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game View");
        Pane pane = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        pane.getChildren().add(canvas);

        primaryStage.setScene(new Scene(pane, WIDTH, HEIGHT));

        GraphicsContext gc = canvas.getGraphicsContext2D();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
