package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.controller.InputHandler;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Camera;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Enemy;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.EntityDrawable;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.Game;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GameView{
    List<EntityDrawable> drawables = new ArrayList<>();

    private Canvas canvas;
    private GraphicsContext gc;
    private Game game;
    private Image background;
    private Scene scene;

    private Camera camera;
    List<BackgroundLayer> backgroundLayers;


    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;


    private double screenCenter = WIDTH / 2.0;
    private double playerX;

    private float chunkWidth = 1280;
    private float lastChunkX = 0;

    public void start(Stage stage) {
        initGame();
        initUI(stage);
        startGameLoop();
    }

    private void initGame() {
        game = new Game();
        game.startGame();
        camera = new Camera(screenCenter);
        game.getPlayer().setCamera(camera);
//        background = new Image(getClass().getResourceAsStream("/images/bgs/background_alpha.png"));
        backgroundLayers = List.of(
                new BackgroundLayer(new Image(getClass().getResourceAsStream("/images/bgs/far_bg_erased.png")), 0.1),
                new BackgroundLayer(new Image(getClass().getResourceAsStream("/images/bgs/mid_bg.png")), 0.5),
                new BackgroundLayer(new Image(getClass().getResourceAsStream("/images/bgs/near_background_alpha.png")), 1)

        );
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
                if (playerX + WIDTH > lastChunkX) {
                    float newChunkStart = lastChunkX;
                    float newChunkEnd = newChunkStart + chunkWidth;
                    game.generateChunk(newChunkStart, newChunkEnd);
                    lastChunkX = newChunkEnd;
                }

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
//        System.out.println("cameraX: " + camera.getX() + " | playerX: " + playerX);
//        System.out.println("playerY" + game.getPlayer().getY());
        double newPlayerX = game.getPlayer().getX();
        if (newPlayerX > playerX) {
            camera.update(newPlayerX);
        }
        playerX = newPlayerX;
    }
    private void renderBackground() {
        for (BackgroundLayer layer : backgroundLayers) {
            layer.render(gc, camera, WIDTH, HEIGHT);
        }
    }

//    private void renderEntities() {
//        game.getPlayer().render(gc, camera.getX());
//        for (Enemy enemy : game.getEnemies()) {
//            enemy.render(gc, camera.getX());
//        }
//    }
    private void renderEntities() {
        drawables.clear();
        drawables.addAll(game.getEnemies());
        drawables.add(game.getPlayer());

        drawables.sort(Comparator.comparing(EntityDrawable::getRenderY));

        for (EntityDrawable d : drawables) {
//            System.out.println(d.getClass().getSimpleName() + " Y=" + d.getRenderY());
            if (d instanceof Enemy enemy) {
                enemy.render(gc, camera.getX(), game.getPlayer());
            } else {
                d.render(gc, camera.getX());
            }
        }
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
        scene.setOnMousePressed(event -> {
            inputHandler.handleMousePressed(event);
        });
        scene.setOnMouseReleased(event -> {
            inputHandler.handleMouseReleased(event);
        });

    }
    public int getWIDTH(){
        return WIDTH;
    }
    public int getHEIGHT(){
        return HEIGHT;
    }
}

