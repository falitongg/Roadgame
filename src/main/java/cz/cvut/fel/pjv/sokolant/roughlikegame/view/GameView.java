package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.controller.InputHandler;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateSaver;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

    private ItemType type;
    Image icon = Inventory.getIcon(type);


    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;


    private double screenCenter = WIDTH / 2.0;
    private double playerX;


    private long gameOverStartTime = 0;
    private boolean transitionScheduled = false;
    private Runnable returnToMenuCallback;
    private AnimationTimer gameLoop;

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
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.update(camera.getX());
                updateCamera();

                if (playerX + WIDTH > game.getLastChunkX()) {
                    float newChunkStart = game.getLastChunkX();
                    float newChunkEnd = newChunkStart + WIDTH;
                    game.generateChunk(newChunkStart, newChunkEnd);
                    game.setLastChunkX(newChunkEnd);
                }


                if (game.getState() == GameState.GAME_OVER && !transitionScheduled) {
                    gameOverStartTime = System.currentTimeMillis();
                    transitionScheduled = true;
                }

                render();

                if (transitionScheduled) {
                    long nowMillis = System.currentTimeMillis();
                    if (nowMillis - gameOverStartTime >= 5000) {
                        transitionScheduled = false;
                        gameLoop.stop();
                        if (returnToMenuCallback != null) {
                            returnToMenuCallback.run();
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }



    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        renderBackground();
        renderEntities();
        drawPlayerHud(gc, game.getPlayer(), camera.getX());

        if (game.getState() == GameState.GAME_OVER) {
            gc.setGlobalAlpha(0.9);
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setGlobalAlpha(1.0);

            String text = "DEAD";
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Impact", FontWeight.BOLD, 96));

            Text tempText = new Text(text);
            tempText.setFont(gc.getFont());
            double textWidth = tempText.getLayoutBounds().getWidth();

            gc.fillText(text, (canvas.getWidth() - textWidth) / 2, canvas.getHeight() / 2);
        }
    }

    //posun kamery s hracem
    private void updateCamera() {
//        System.out.println("cameraX: " + camera.getX() + " | playerX: " + playerX)
//        System.out.println(getHEIGHT());
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

    private void renderEntities() {
        drawables.clear();
        drawables.addAll(game.getEnemies());
        drawables.addAll(game.getObstacles());
        drawables.addAll(game.getTraders());
        drawables.add(game.getPlayer());

//        System.out.println("---- SORTED DRAWABLES ----");
//        for (EntityDrawable d : drawables) {
//            System.out.println(d.getClass().getSimpleName() + ": " + d.getRenderY());
//        }

        drawables.sort(Comparator.comparing(EntityDrawable::getRenderY));

        for (EntityDrawable d : drawables) {
            d.render(gc, camera.getX(), game.getPlayer());
            if (d instanceof Enemy enemy) {
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

        if (game.getState() == GameState.TRADE) {
            Trader tr = game.getCurrentTrader();
            gc.setFill(Color.rgb(0,0,0,0.7));
            gc.fillRect(100, 100, 300, 300);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Consolas", 20));
            int y = 140;
            for (int i = 0; i < tr.getItems().size(); i++) {
                Item it = tr.getItems().get(i);
                gc.fillText("[" + (i + 1) + "] " + it.getName() + "  ...  " + it.getPrice() + "$", 120, y);
                y += 30;
            }
            gc.fillText("Esc – exit", 120, y + 10);
        }

        //inventory bar
        drawInventoryBar(gc, game.getPlayer());

        //money
        drawMoneyHud(gc, game.getPlayer(), canvas.getWidth());


    }
    private void drawInventoryBar(GraphicsContext gc, Player player) {
        double startX = 20;
        double y = 20;
        double spacing = 80;

        ItemType[] types = {
                ItemType.BANDAGE,
                ItemType.WATER,
                ItemType.ARMOR,
                ItemType.BOXER,
                ItemType.KEYCARD
        };

        for (int i = 0; i < types.length; i++) {
            ItemType type = types[i];
            int count = player.getInventory().getCount(type);
            Image icon = Inventory.getIcon(type);

            if (icon != null) {
                gc.drawImage(icon, startX + i * spacing, y, 32, 32);
            }

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
            gc.fillText("[" + (i + 1) + "] (" + count + ")", startX + i * spacing, y + 48);
        }
    }
    private void drawMoneyHud(GraphicsContext gc, Player player, double canvasWidth) {
        int money = player.getMoney();
        String moneyText = "💵 " + money + " $";

        gc.setFill(Color.DARKGREEN);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));

        double x = canvasWidth - 100;
        double y = 30;

        gc.fillText(moneyText, x, y);
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

        // bg
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(xOffset, yOffset, barWidth, barHeight);

        // hp
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
        InputHandler inputHandler = new InputHandler(game, camera, this);

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

    public void setReturnToMenuCallback(Runnable callback) {
        this.returnToMenuCallback = callback;
    }
    public void resetAfterLoad() {
        this.playerX = game.getPlayer().getX();
        camera.update(playerX);
    }
}

