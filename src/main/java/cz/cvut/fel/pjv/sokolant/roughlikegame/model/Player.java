package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.VisualState;


public class Player extends Entity implements Renderable {
    private Camera camera;
    private Image playerImage;
    GameView view = new GameView();

    private float worldMinX = -60;
    private float worldMinY = 480;
    private float worldMaxY = view.getHEIGHT()-160;

    private float stamina;// Energy for running and
    private float thirst;// Increases over time, affects
    private float hunger;// Like starvation, but grows faster, also harmful
    private float armor;// Defense — reduces the damage received
    private float radiation; // Radiation level — causes damage over time
    private float speed;//Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).
    private final Inventory inventory;
    private Direction currentDirection = Direction.DOWN;
    private VisualState currentState = VisualState.IDLE;

    // Fyzika skákání
    private double velocityY = 0; //vertikalni rychlost
    private final double gravity = 2; //jak rychle se hrac bude pochybovat dolu
    private final double jumpStrength = -18; //pocatecny vystrel vzhuru
    private boolean onGround = true; //zda je na zemi
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private double velocityX = 0; //horizontalni rychlost


    // Úroveň země
    private double lastGroundY = 530;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }


    public Player(float x, float y, float health, float damage, Inventory inventory, float speed, float radiation, float armor, float hunger, float thirst, float stamina) {
        super(x, y, health, damage);
        this.inventory = inventory;
        this.speed = speed;
        this.radiation = radiation;
        this.armor = armor;
        this.hunger = hunger;
        this.thirst = thirst;
        this.stamina = stamina;
    }
    public Player() {
        this(100, 500, 100, 10, new Inventory(), 1.0f, 0, 10, 0, 0, 100);
        this.playerImage = new Image(getClass().getResourceAsStream("/images/player/player1.png"));
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(playerImage, getX(), getY(), 160, 200);
    }
    public void render(GraphicsContext gc, double cameraX) {
        gc.drawImage(playerImage, getX() - cameraX, getY(), 160, 200);
    }

    public void initImages(){

    }
    private Image getImageForCurrentState() {
        String direction = currentDirection.name().toLowerCase();
        String state = currentState.name().toLowerCase();

        String path = String.format("/images/player/player1.png", direction, state); //temp
        return new Image(getClass().getResourceAsStream(path));
    }

    //move function
    public void move(Direction direction) {
        //TODO реализовать перемещение игрока

        final double STEP = 13;

        this.currentDirection = direction;
        this.currentState = VisualState.MOVING;

        switch (direction) {
            case UP -> {
                if (onGround && y - STEP >= 470) {
                    y -= STEP;
                }
            }
            case DOWN -> y += STEP;
            case LEFT -> x -= STEP;
            case RIGHT -> x += STEP;
            case SPACE -> jump();
        }
        clampToBounds();

    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void clampToBounds() {
        double newX = getX();
        double newY = getY();
        if (camera != null && newX < camera.getX()) {
            newX = (float) camera.getX();
        }


        if (newX < worldMinX) newX = worldMinX;

//        // Omezit pohyb nahoru jen když není ve skoku
//        if (onGround && newY < worldMinY) {
//            newY = worldMinY;
//        }

        if (newY > worldMaxY) newY = worldMaxY;

        setX(newX);
        setY(newY);
    }

    public void jump() {
        if (onGround) {
            lastGroundY = y;
            velocityY = jumpStrength;
            if(movingLeft){
                velocityX = -speed*5;
            }
            else if(movingRight){
                velocityX = speed*5;
            }else velocityX = 0;

            onGround = false;
        }

    }
    //sprint function
    public void sprint(Direction direction) {
        //TODO реализовать бег игрока
    }
    //permadeath function
    public void die() {
        //TODO реализовать смерть игрока как конец игры
    }
    public void update() {
        if (!onGround) {
            velocityY += gravity;
            y += velocityY;
            x += velocityX;

            if (y >= lastGroundY) {
                y = lastGroundY;
                velocityY = 0;
                velocityX = 0;
                onGround = true;
            }
        }

        clampToBounds();
    }

    public void useItemFromInventory(int index) {
        Item item = inventory.getItem(index);
        if (item != null) {
            useItem(item);
        }
    }

    //use item function
    public void useItem(Item item) {
        //TODO реализовать функцию использования предмета
    }


}

