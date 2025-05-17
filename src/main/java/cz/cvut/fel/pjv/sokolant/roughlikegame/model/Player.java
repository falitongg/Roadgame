package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ItemType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.ObstacleType;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.VisualState;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import static javafx.util.Duration.millis;


public class Player extends Entity implements EntityDrawable {

    private Image[] walkRightFrames;
    private Image[] walkLeftFrames;

    private Image[] blockWalkRightFrames;
    private Image[] blockWalkLeftFrames;

    private Image[] sprintRightFrames;
    private Image[] sprintLeftFrames;

    private Image[] crouchRightFrames;
    private Image[] crouchLeftFrames;



    private double lastStepX = -1;
    private double stepDistance = 25;
    private double lastStepY = -1;
    private long lastFireDamageTime = 0;

    private int walkFrameIndex = 0;

    private Direction lastHorizontalDirection = Direction.RIGHT;

    private boolean isWalking = false;
    private boolean isBlocking = false;
    private boolean isSprinting = false;
    private boolean isCrouching = false;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;



    private Image playerImageLeft;
    private Image playerImageRight;
    private Image playerImageJumpRight;
    private Image playerImageJumpLeft;
    private Image playerImageBlockingRight;
    private Image playerImageBlockingLeft;
    private Image damageEffect;
    private Image lightingDecrEffect;


    private Image playerAttackLeft;
    private Image playerAttackRight;


    private Camera camera;
    private Image playerImage;

    private float worldMinX = -60;
    private float worldMinY = 480;
    private float worldMaxY = 720-160;

    private float stamina;// Energy for running and
    private float thirst;// Increases over time, affects
    private float hunger;// Like starvation, but grows faster, also harmful
    private float armor;// Defense — reduces the damage received
    private float radiation; // Radiation level — causes damage over time
    private float speed;//Affects movement speed, may vary depending on the player's state (e.g. 0 stamina - temporary inability to move).
    private final Inventory inventory;
    private int money;
    private Direction currentDirection = Direction.DOWN;
    private VisualState currentState = VisualState.IDLE;

    // Fyzika skákání
    private double velocityY = 0; //vertikalni rychlost
    private final double gravity = 2; //jak rychle se hrac bude pochybovat dolu
    private final double jumpStrength = -18; //pocatecny vystrel vzhuru
    private boolean onGround = true; //zda je na zemi
    private double velocityX = 0; //horizontalni rychlost
    private float maxHealth = 100f;

    private boolean isAttacking = false;
    float damageReductionFactor;
    private boolean hasKnuckle = false;
    private boolean staminaBoostActive = false;

    // Úroveň země
    private double lastGroundY = 530;
    final double ATTACK_RANGE = 80;
    private static final float PLAYER_WIDTH  = 48;
    private static final float PLAYER_HEIGHT = 96;

    private Game game;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }


    public Player(float x, float y, float health, float damage, Inventory inventory, float speed,  float armor, float stamina, int money) {
        super(x, y, health, damage);
        this.inventory = inventory;
        this.speed = speed;
//        this.radiation = radiation;
        this.armor = armor;
//        this.hunger = hunger;
//        this.thirst = thirst;
        this.stamina = stamina;
        this.money = money;
        this.width  = PLAYER_WIDTH;
        this.height = PLAYER_HEIGHT;
    }
    public Player() {
        this(100, 500, 100, 33, new Inventory(), 1.0f, 100, 100, 0);
//        this.playerImage = new Image(getClass().getResourceAsStream("/images/player/player_idle_right.png"));
        this.width  = PLAYER_WIDTH;
        this.height = PLAYER_HEIGHT;
        this.currentDirection = Direction.RIGHT;
        this.playerImageLeft = new Image(getClass().getResourceAsStream("/images/player/player_idle_left.png"));
        this.playerImageRight = new Image(getClass().getResourceAsStream("/images/player/player_idle_right.png"));
        playerAttackLeft = new Image(getClass().getResourceAsStream("/images/player/player_attack_left.png"));
        playerAttackRight = new Image(getClass().getResourceAsStream("/images/player/player_attack_right.png"));
        playerImageJumpRight = new Image(getClass().getResourceAsStream("/images/player/player_jump_right.png"));
        playerImageJumpLeft = new Image(getClass().getResourceAsStream("/images/player/player_jump_left.png"));
        playerImageBlockingRight = new Image(getClass().getResourceAsStream("/images/player/player_blocking_right.png"));
        playerImageBlockingLeft = new Image(getClass().getResourceAsStream("/images/player/player_blocking_left.png"));

        damageEffect = new Image(getClass().getResourceAsStream("/images/effects/heart_decr.png"));
        lightingDecrEffect = new Image(getClass().getResourceAsStream("/images/effects/red_lighting.png"));

        walkRightFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_step1_r.png")),
                playerImageRight,
                new Image(getClass().getResourceAsStream("/images/player/player_step2_r.png")),
                playerImageRight
        };

        walkLeftFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_step1_l.png")),
                playerImageLeft,
                new Image(getClass().getResourceAsStream("/images/player/player_step2_l.png")),
                playerImageLeft

        };
        blockWalkRightFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_block_right_step1.png")),
                playerImageBlockingRight,
                new Image(getClass().getResourceAsStream("/images/player/player_block_right_step2.png")),
                playerImageBlockingRight
        };

        blockWalkLeftFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_block_left_step1.png")),
                playerImageBlockingLeft,
                new Image(getClass().getResourceAsStream("/images/player/player_block_left_step2.png")),
                playerImageBlockingLeft
        };
        sprintRightFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_run_2_r.png")),
                new Image(getClass().getResourceAsStream("/images/player/player_run_1_r.png"))
        };
        sprintLeftFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_run_2_l.png")),
                new Image(getClass().getResourceAsStream("/images/player/player_run_1_l.png"))


        };
        crouchRightFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_ctrl_1_r.png")),
                new Image(getClass().getResourceAsStream("/images/player/player_ctrl_2_r.png"))
        };

        crouchLeftFrames = new Image[]{
                new Image(getClass().getResourceAsStream("/images/player/player_ctrl_1_l.png")),
                new Image(getClass().getResourceAsStream("/images/player/player_ctrl_2_l.png"))
        };



    }

    @Override
    public void render(GraphicsContext gc, double cameraX, Player player) {
        render(gc, cameraX);
    }

    public void render(GraphicsContext gc, double cameraX) {
        Image img;

        if (isFlashing) {
            gc.setGlobalAlpha(0.6);
            gc.drawImage(damageEffect, getX() - cameraX + 5, getY() + 10, 50, 50); // подгони под спрайт
            gc.setGlobalAlpha(1.0);
        }
        else if (stamina < 20f) {
            gc.setGlobalAlpha(0.5);
            gc.drawImage(lightingDecrEffect, getX() - cameraX + 5, getY() + 5, 48, 48);
            gc.setGlobalAlpha(1.0);
        }
        boolean isMoving = movingLeft || movingRight || currentDirection == Direction.UP || currentDirection == Direction.DOWN;
        if (isSprinting) {
            stepDistance = 100;
        } else if (isCrouching) {
            stepDistance = 75;
        } else {
            stepDistance = 25;
        }

        if (isBlocking) {
            if (isMoving) {
                // player is moving while blocking
                if (Math.abs(getX() - lastStepX) >= stepDistance || Math.abs(getY() - lastStepY) >= stepDistance) {
                    walkFrameIndex = (walkFrameIndex + 1) % blockWalkRightFrames.length;
                    lastStepX = getX();
                    lastStepY = getY();
                }

                img = lastHorizontalDirection == Direction.LEFT
                        ? blockWalkLeftFrames[walkFrameIndex]
                        : blockWalkRightFrames[walkFrameIndex];

            } else {
                // player is blocking
                img = lastHorizontalDirection == Direction.LEFT
                        ? playerImageBlockingLeft
                        : playerImageBlockingRight;
            }

        }

        else if (isAttacking) {
            img = lastHorizontalDirection == Direction.LEFT ?
                    playerAttackLeft : playerAttackRight;
        }
        else if (!onGround) {
            //jump
            img = switch (currentDirection) {
                case LEFT -> playerImageJumpLeft;
                case RIGHT -> playerImageJumpRight;
                default -> playerImageJumpRight;
            };
        }  else if (isMoving) {
            if (Math.abs(getX() - lastStepX) >= stepDistance || Math.abs(getY() - lastStepY) >= stepDistance) {
                walkFrameIndex = (walkFrameIndex + 1) % 4;
                lastStepX = getX();
                lastStepY = getY();
            }

            if (isCrouching) {
                img = currentDirection == Direction.LEFT ?
                        crouchLeftFrames[walkFrameIndex % crouchLeftFrames.length] : crouchRightFrames[walkFrameIndex % crouchRightFrames.length];
            }
            else if (isSprinting) {
                img = currentDirection == Direction.LEFT
                        ? sprintLeftFrames[walkFrameIndex % sprintLeftFrames.length]
                        : sprintRightFrames[walkFrameIndex % sprintRightFrames.length];
            }
            else {
                img = currentDirection == Direction.LEFT
                        ? walkLeftFrames[walkFrameIndex]
                        : walkRightFrames[walkFrameIndex];
            }
        }

        else {
            if (isCrouching) {
                img = lastHorizontalDirection == Direction.LEFT
                        ? crouchLeftFrames[0]
                        : crouchRightFrames[0];
            } else {
                img = switch (currentDirection) {
                    case LEFT -> playerImageLeft;
                    case RIGHT -> playerImageRight;
                    default -> lastHorizontalDirection == Direction.LEFT
                            ? playerImageLeft
                            : playerImageRight;
                };
            }
        }


        gc.drawImage(img, getX() - cameraX, getY());

    }


    public void initImages(){

    }
    @Override
    public double getRenderY() {
        return getY() ;
    }

    //move function
    public void move(Direction direction) {

        double step = isCrouching ? 3 : (isSprinting ? 12 : 7);

        this.currentDirection = direction;

        this.currentState = VisualState.MOVING;

        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            lastHorizontalDirection = direction;
        }

        switch (direction) {
            case UP -> {
                if (onGround && y - step >= 467) {
                    y -= step;
                }
            }
            case DOWN -> y += step;
            case LEFT -> x -= step;
            case RIGHT -> x += step;
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

        if (newY > worldMaxY) newY = worldMaxY;

        setX(newX);
        setY(newY);
    }


    public void jump() {
        if (!onGround || isCrouching || stamina < 30f) return;

        spendStamina(25f);
        lastGroundY = y;
        velocityY = jumpStrength;

        if (movingLeft) {
            velocityX = -speed * 5;
        } else if (movingRight) {
            velocityX = speed * 5;
        } else {
            velocityX = 0;
        }

        onGround = false;
    }
    //permadeath function
    public void die() {
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

        double dx = 0;
        double dy = 0;

        double step = isCrouching ? 1.5 : (isSprinting ? 6 : (isBlocking ? 1.5 : 2.5));

        if (isSprinting) {
            spendStamina(0.8f);
            if (stamina <= 0) {
                setSprinting(false);
            }
        } else if (onGround && !isAttacking) {
            if (staminaBoostActive) {
                restoreStamina(1f);
            }
            else if (isBlocking) {
                restoreStamina(0.1f);
            } else {
                restoreStamina(0.4f);
            }
        }


        if (movingUp && onGround && y - step >= 467) dy -= step;
        if (movingDown) dy += step;
        if (movingLeft) dx -= step;
        if (movingRight) dx += step;

// normalizace
        if (dx != 0 && dy != 0) {
            dx *= 0.7071;
            dy *= 0.7071;
        }

        x += dx;
        y += dy;

        if (dx < 0) {
            currentDirection = Direction.LEFT;
            lastHorizontalDirection = Direction.LEFT;
        } else if (dx > 0) {
            currentDirection = Direction.RIGHT;
            lastHorizontalDirection = Direction.RIGHT;
        } else if (dy < 0) {
            currentDirection = Direction.UP;
        } else if (dy > 0) {
            currentDirection = Direction.DOWN;
        }


        clampToBounds();

        long now = System.currentTimeMillis();

        for (Obstacle o : game.getObstacles()) {
            if (o.getType() != ObstacleType.FIRE) continue;

            float fireDx = (float) (this.getX() - o.getX());
            float fireDy = (float) (this.getY() - o.getY());
            double dist = Math.sqrt(fireDx * fireDx + fireDy * fireDy);

            if (dist <= 100 && now - lastFireDamageTime >= 1000) {
                takeDamage(25f);
                lastFireDamageTime = now;
            }
        }

    }


    //use item function
    public void takeDamage(float amount) {
        flash();

        if(armor != 0) {
            damageReductionFactor = 0.5f;
        }else {
            damageReductionFactor = 1f;
        }
        float effectiveDamage = amount * damageReductionFactor;
        if(isBlocking){
            this.health -= amount * 0.3f;
            armor -= amount * 0.3f;

        }
        else{
            this.health -= effectiveDamage;
            armor -= amount * 0.8f;

        }


        if (armor < 0) armor = 0;

        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }
    public void attack(List<Enemy> enemies) {

        if (stamina < 20f) return;

        if (isBlocking) {
            isBlocking = false;
        }

        spendStamina(10f);
        isAttacking = true;

        new Timeline(
                new KeyFrame(
                        millis(200),
                        e -> isAttacking = false
                )
        ).play();

        for (Enemy enemy : enemies) {
            double dx = enemy.getX() - this.getX();
            double dy = enemy.getY() - this.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= ATTACK_RANGE) {
                flash();
                enemy.takeDamage(this.getDamage());
                break;
            }
        }

        for (Obstacle o : game.getObstacles()) {
            if (!hasKnuckleEquipped()) break;
            if (o.getType()!=ObstacleType.BOX && o.getType()!=ObstacleType.BOX_SMALL) continue;

            // точка удара игрока
            double attackOriginX = getX() + getWidth();
            double attackOriginY = getY() + getHeight()*0.7;

            // смещения от этой точки до коробки
            double dx = o.getX() - attackOriginX;
            double dy = o.getY() - attackOriginY;

            // зона в 80px по X и 80px по Y
            boolean inFront = lastHorizontalDirection==Direction.RIGHT ?
                    (dx>=0 && dx<=80) : (dx<=0 && dx>=-80);
            boolean inHeight = Math.abs(dy) <= 70;

            if (inFront && inHeight) {
                o.takeDamage(getDamage());
                game.spawnItem(o.getX(), o.getY());
                break;
            }
        }




    }
    public void restoreHealth(float amount) {
        this.health += amount;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    public void restoreStamina(float amount) {
        this.stamina += amount;
        if (this.stamina > 100f) {
            this.stamina = 100f;
        }
    }

    public void spendStamina(float amount) {
        this.stamina -= amount;
        if (this.stamina < 0f) {
            this.stamina = 0f;
        }
    }
    public void setBlocking(boolean blocking) {
        if (isSprinting && blocking) {
            return;
        }
        this.isBlocking = blocking;
        if (blocking) {
            this.isSprinting = false;
        }
    }


    public void setSprinting(boolean sprinting) {
        if (sprinting) {
            this.isBlocking = false;
        }
        this.isSprinting = sprinting && !isBlocking && !isCrouching;
    }

    public void setCrouching(boolean crouching) {
        this.isCrouching = crouching;
        if (crouching) {
            this.isSprinting = false;
        }
    }
    public void setMovingUp(boolean movingUp) { this.movingUp = movingUp; }
    public void setMovingDown(boolean movingDown) { this.movingDown = movingDown; }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getArmor() {
        return armor;
    }
    public float getStamina() {
        return stamina;
    }

    public void setArmor(float armor) {
        if (this.armor + armor > 100) this.armor = 100;
        else this.armor = armor;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }
    public int getMoney() {
        return money;
    }
    public void addMoney(int amount) {
        money += amount;
    }
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {

    }
    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public Rectangle2D getBounds() {                     // для коллизии
        return new Rectangle2D(getX() , getY() - PLAYER_HEIGHT,  width, height);
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public String useItem(ItemType type) {
        if (!inventory.hasItem(type)) return "Not enough " + type.name();

        switch (type) {
            case BANDAGE -> {
                restoreHealth(50);
            }
            case WATER -> {
                restoreStamina(50);
            }
            case ARMOR -> {
                setArmor(getArmor() + 50);
            }
            case BOXER -> {
                equipKnuckle();
            }
            case BUCKET -> {
                useWaterBucket();
            }
        }

        if(type == ItemType.BOXER) return getInventory().getItemMessage(type);
        else inventory.remove(type);
        return getInventory().getItemMessage(type);
    }
    public void resetMovement() {
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }
    public void equipKnuckle() {
        if(!hasKnuckle) {
            hasKnuckle = true;
            this.damage = 50;
        }else{
            hasKnuckle = false;
            this.damage = 33;
        }
    }

    public boolean hasKnuckleEquipped() {
        return hasKnuckle;
    }

    public void pickUpItem(){
        float distance = 80f;
        Iterator<Item> iterator = game.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();

            float dx = (float) (getX() - item.getX());

            if(Math.abs(dx) < distance) {
                inventory.add(item);
                iterator.remove();
                break;
            }
        }
    }
    public void useWaterBucket() {
        staminaBoostActive = true;
        restoreHealth(25);
        restoreStamina(100);
        new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    staminaBoostActive = false;
                })
        ).play();
        System.out.println("water");
        for (Obstacle o : game.getObstacles()) {
            if (o.getType() != ObstacleType.FIRE) continue;

            // точка действия — как будто "плеснул вперёд"
            double attackOriginX = getX() + getWidth();
            double attackOriginY = getY() + getHeight()*0.7;

            // смещения от этой точки до костра
            double dx = o.getX() - attackOriginX;
            double dy = o.getY() - attackOriginY;

            boolean inFront = lastHorizontalDirection==Direction.RIGHT ?
                    (dx>=0 && dx<=80) : (dx<=0 && dx>=-80);

            boolean inHeight = Math.abs(dy) <= 70;

            if (inFront && inHeight) {
                game.getObstacles().remove(o);
                System.out.println("🔥");
                return;
            }
        }

    }


}

