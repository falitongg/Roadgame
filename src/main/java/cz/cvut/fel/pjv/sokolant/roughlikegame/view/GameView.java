package cz.cvut.fel.pjv.sokolant.roughlikegame.view;

import cz.cvut.fel.pjv.sokolant.roughlikegame.controller.InputHandler;
import cz.cvut.fel.pjv.sokolant.roughlikegame.data.GameStateSaver;
import cz.cvut.fel.pjv.sokolant.roughlikegame.model.*;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.EnemyType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.GameState;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

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
    private boolean alreadyLoaded = false;

    private String notificationMessage = "";
    private long notificationMessageTime = 0;

    /**
     * Default constructor used primarily for initialization purposes.
     */
    public GameView() {}

    /**
     * Constructs a GameView with pre-loaded game state and camera.
     *
     * @param game   an existing game instance to load
     * @param camera the camera instance controlling viewport
     */
    public GameView(Game game, Camera camera) {
        alreadyLoaded = true;
        this.game = game;
        this.camera = camera;
        game.getPlayer().setCamera(camera);
        initBackgroundLayers();
    }

    /**
     * Starts the game view, initializes UI elements, and launches the game loop.
     *
     * @param stage primary stage for JavaFX
     */
    public void start(Stage stage) {
        if (!alreadyLoaded) {
            initGame();
        }
        initUI(stage);
        startGameLoop();
    }

    private void initGame() {
        game = new Game();
        game.startGame();
        camera = new Camera(screenCenter);
        game.getPlayer().setCamera(camera);
        initBackgroundLayers();
    }

    /**
     * Initializes background layers for parallax scrolling effect.
     */
    public void initBackgroundLayers() {
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

    /**
     * Starts the main animation loop of the game, handling updates and rendering.
     */
    private void startGameLoop() {
        // Animation timer setup and logic
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
                    transitionScheduled = false;
                    gameLoop.stop();

                    if (game.getState() == GameState.MENU) {
                        returnToMenuCallback.run();
                    }
                    new Timeline(
                            new KeyFrame(
                                    Duration.seconds(2), e -> {
                                if (returnToMenuCallback != null) {
                                    returnToMenuCallback.run();
                                }
                            }
                            )
                    ).play();
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Renders the current state of the game including UI elements, player, and entities.
     */
    private void render() {
        if (game.getState() == GameState.MENU) {
            // clears and draws the menu
            gc.setGlobalAlpha(0.9);
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Impact", FontWeight.BOLD, 48));
            gc.fillText("PAUSED", WIDTH / 2 - 100, HEIGHT / 2);

            String prompt = "Switch to main menu?  [Y] / [N]";
            gc.setFont(Font.font("Consolas", FontWeight.NORMAL, 24));
            double promptWidth = new Text(prompt).getLayoutBounds().getWidth();
            gc.fillText(prompt, (WIDTH - 500) / 2, HEIGHT / 2 + 40);

            return;
        }

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        renderBackground();
        renderEntities();
        drawPlayerHud(gc, game.getPlayer(), camera.getX());

        if (game.getState() == GameState.GAME_OVER) {
            gc.setGlobalAlpha(0.9);
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
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

    /**
     * Updates camera position to follow the player's movements.
     */
    private void updateCamera() {
        // Camera following logic
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

    /**
     * Renders all visible game entities on the screen.
     */
    private void renderEntities() {
        // Rendering entities logic
        drawables.clear();
        drawables.addAll(game.getEnemies());
        drawables.addAll(game.getObstacles());
        drawables.addAll(game.getItems());
        drawables.addAll(game.getTraders());
        drawables.add(game.getPlayer());

        drawables.sort(Comparator.comparing(EntityDrawable::getRenderY));

        for (EntityDrawable d : drawables) {
            d.render(gc, camera.getX(), game.getPlayer());
            if (d instanceof Enemy enemy) {
                drawEnemyHealthBar(gc, enemy, camera.getX());
            }
        }



    }

    /**
     * Renders the player's HUD (Head-Up Display) on the screen.
     *
     * @param gc        used for rendering
     * @param player    player's status
     * @param cameraX   the current horizontal offset of the camera, used to align HUD elements correctly
     */
    private void drawPlayerHud(GraphicsContext gc, Player player, double cameraX) {
        // Player HUD drawing logic
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
            gc.fillRect(100, 100, 300, 200);

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

        //notification
        if (!notificationMessage.isEmpty() && System.currentTimeMillis() - notificationMessageTime < 2000) {
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Consolas", 20));
            gc.fillText(notificationMessage, 20, 100);
        } else {
            notificationMessage = "";
        }
    }

    /**
     * Renders the player's inventory bar in the top-left corner of the screen.
     *
     * Displays icons for predefined item types (bandage, water, armor, etc.) along with their
     * quantity in the player's inventory. Each item slot is labeled with its corresponding hotkey (1–5).
     *
     * @param gc        used to draw the inventory bar
     * @param player    his inventory is being displayed
     */
    private void drawInventoryBar(GraphicsContext gc, Player player) {
        // Inventory bar rendering logic
        double startX = 20;
        double y = 20;
        double spacing = 80;

        ItemType[] types = {
                ItemType.BANDAGE,
                ItemType.WATER,
                ItemType.ARMOR,
                ItemType.BOXER,
                ItemType.BUCKET
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

    /**
     * Renders the player's current amount of money in the top-right corner of the screen.
     *
     * Displays the money as a green text label with a dollar icon and value,
     * aligned relative to the total canvas width.
     *
     * @param gc            used to render the HUD
     * @param player        whose money is being rendered
     * @param canvasWidth   the width of the canvas, used to position the money display on the right side
     */
    private void drawMoneyHud(GraphicsContext gc, Player player, double canvasWidth) {
        // Money HUD drawing logic
        int money = player.getMoney();
        String moneyText = "💵 " + money + " $";

        gc.setFill(Color.DARKGREEN);
        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 24));

        double x = canvasWidth - 100;
        double y = 30;

        gc.fillText(moneyText, x, y);
    }

    /**
     * Renders a health bar above the given enemy entity.
     *
     * The health bar is positioned based on the enemy's location and camera offset,
     * and its width is scaled proportionally to the enemy's current health.
     * Different spacing and width are used for certain enemy types (e.g., dogs).
     *
     * @param gc        used for drawing
     * @param enemy     whose health bar is being rendered
     * @param cameraX   the current horizontal offset of the camera
     */
    private void drawEnemyHealthBar(GraphicsContext gc, Enemy enemy, double cameraX) {
        // Enemy health bar rendering logic
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

    /**
     * Sets up canvas and graphics context for rendering.
     */
    public void setupCanvasAndGraphics(){
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

    }

    /**
     * Initializes JavaFX Scene with canvas and stage properties.
     *
     * @param stage primary JavaFX stage
     */
    public void setupScene(Stage stage) {
        Pane root = new Pane();
        root.getChildren().add(canvas);

        scene = new Scene(root, WIDTH, HEIGHT);

        stage.setTitle("ROAD");
        stage.setScene(scene);
    }

    /**
     * Initializes input handlers for player actions.
     */
    public void setupInputHandling(){
        InputHandler inputHandler = new InputHandler(game, camera, this);
        //references to the inputHandler class methods
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

    /**
     * Sets the callback method to return to the main menu.
     *
     * @param callback Runnable executed when returning to main menu
     */
    public void setReturnToMenuCallback(Runnable callback) {
        this.returnToMenuCallback = callback;
    }

    /**
     * Resets the view after loading a saved game state.
     */
    public void resetAfterLoad() {
        this.playerX = game.getPlayer().getX();
        camera.update(playerX);
    }

    /**
     * Shows an on-screen notification message to the player.
     *
     * @param message message text to be displayed
     */
    public void showNotificatrion(String message) {
        this.notificationMessage = message;
        this.notificationMessageTime = System.currentTimeMillis();
    }

    public Game getGame() {
        return game;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setTransitionScheduled(boolean transitionScheduled) {
        this.transitionScheduled = transitionScheduled;
    }
}

