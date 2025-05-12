package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.controller.InputHandler;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType.DOG;


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
        drawPlayerHud(gc, game.getPlayer(), camera.getX());
    }
    //posun kamery s hracem
    private void updateCamera() {
//        System.out.println("cameraX: " + camera.getX() + " | playerX: " + playerX);
        System.out.println("playerY" + game.getPlayer().getY());
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

    private void renderEntities() {
        drawables.clear();
        drawables.addAll(game.getEnemies());
        drawables.addAll(game.getObstacles());
        drawables.add(game.getPlayer());

//        System.out.println("---- SORTED DRAWABLES ----");
//        for (EntityDrawable d : drawables) {
//            System.out.println(d.getClass().getSimpleName() + ": " + d.getRenderY());
//        }

        drawables.sort(Comparator.comparing(EntityDrawable::getRenderY));

        for (EntityDrawable d : drawables) {
            d.render(gc, camera.getX(), game.getPlayer());
        }
        for (EntityDrawable d : drawables) {
            if (d instanceof Enemy enemy && enemy.getHealth() > 0) {
                drawEnemyHealthBar(gc, enemy, camera.getX());
            }
        }

    }
    private void drawPlayerHud(GraphicsContext gc, Player player, double cameraX) {
        double barWidth = 50;
        double barHeight = 6;
        double xOffset = player.getX() - cameraX + 80 - barWidth / 2;
        double yOffset = player.getY() + 40;

        //Health
        double healthRatio = player.getHealth() / player.getMaxHealth();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(xOffset, yOffset-10, barWidth, barHeight);
        gc.setFill(Color.RED);
        gc.fillRect(xOffset, yOffset - 10, barWidth * healthRatio, barHeight);

        //Armor
        double armorRatio = player.getArmor() / 100.0;
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(xOffset, yOffset - 20, barWidth, barHeight);
        gc.setFill(Color.BLUE);
        gc.fillRect(xOffset, yOffset - 20, barWidth * armorRatio, barHeight);

        //Stamina
        double staminaRatio = player.getStamina() / 100.0;
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(xOffset, yOffset, barWidth, barHeight);
        gc.setFill(Color.CYAN);
        gc.fillRect(xOffset, yOffset, barWidth * staminaRatio, barHeight);
    }
    private void drawEnemyHealthBar(GraphicsContext gc, Enemy enemy, double cameraX) {
        double barWidth;
        double barHeight = 5;
        double spacingAbove = 40;


        if (enemy.getType() == EnemyType.DOG) {
            spacingAbove += 50;
            barWidth = 20;
        }else barWidth = 40;

        double xOffset = enemy.getX() - cameraX + 80 - barWidth / 2;
        double yOffset = enemy.getY() + spacingAbove;

        double healthRatio = enemy.getHealth() / enemy.getMaxHealth();

        // Фон
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(xOffset, yOffset, barWidth, barHeight);

        // Полоса HP
        gc.setFill(Color.RED);
        gc.fillRect(xOffset, yOffset, barWidth * healthRatio, barHeight);
    }

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

        scene.setOnKeyPressed(inputHandler::handleInput);

        scene.setOnKeyReleased(inputHandler::handleKeyReleased);

        scene.setOnMousePressed(inputHandler::handleMousePressed);

        scene.setOnMouseReleased(inputHandler::handleMouseReleased);


    }
    public int getWIDTH(){
        return WIDTH;
    }
    public int getHEIGHT(){
        return HEIGHT;
    }
}

