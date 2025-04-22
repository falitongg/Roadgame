package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.controller.InputHandler;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;



public class GameView{
    private Canvas canvas;
    private GraphicsContext gc;
    private Game game;
    private Image background;
    private Scene scene;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private double cameraX = 0;
    private double screenCenter = WIDTH / 2.0;
    private float playerX;

    public void start(Stage stage) {
        initGame();
        initUI(stage);
        startGameLoop();
    }

    private void initGame() {
        game = new Game();
        game.startGame();
        background = new Image(getClass().getResourceAsStream("/images/bgs/background_alpha.png"));
    }


    private void initUI(Stage stage) {
        setupCanvasAndGraphics();
        setupScene(stage);
        setupInputHandling();
        stage.show();
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.update();
                updateCamera();
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
    //posun kamery s hracem
    private void updateCamera() {
        playerX = game.getPlayer().getX();
        if(playerX > screenCenter) {
            cameraX = playerX - screenCenter;
        }
        //DEBUG
        System.out.println("cameraX: " + cameraX + " | playerX: " + playerX);

    }
    private void renderBackground() {
        gc.drawImage(background, -cameraX, 0, WIDTH + cameraX, HEIGHT);
    }
    private void renderEntities() {
        game.getPlayer().render(gc, cameraX);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
    public void setupCanvasAndGraphics(){
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

    }
    public void setupScene(Stage stage) {
        Pane root = new Pane();
        root.getChildren().add(canvas);

        scene = new Scene(root, WIDTH, HEIGHT);

        stage.setTitle("ROAD");
        stage.setScene(scene);
    }
    public void setupInputHandling(){
        InputHandler inputHandler = new InputHandler(game);

        scene.setOnKeyPressed(event -> {
            inputHandler.handleInput(event);
        });
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case A -> game.getPlayer().setMovingLeft(false);
                case D -> game.getPlayer().setMovingRight(false);
            }
        });
    }
    public int getWIDTH(){
        return WIDTH;
    }
    public int getHEIGHT(){
        return HEIGHT;
    }
}

