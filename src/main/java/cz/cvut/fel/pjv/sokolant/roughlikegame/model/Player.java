package cz.cvut.fel.pjv.sokolant.roughlikegame.model;

import cz.cvut.fel.pjv.sokolant.roughlikegame.util.Direction;
import cz.cvut.fel.pjv.sokolant.roughlikegame.view.GameView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.pjv.sokolant.roughlikegame.util.VisualState;

import java.util.List;


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
    private final double stepDistance = 25;
    private double lastStepY = -1;

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



    private Image playerAttackLeft;
    private Image playerAttackRight;


    private Camera camera;
    private Image playerImage;
    GameView view = new GameView();

    private float worldMinX = -60;
    private float worldMinY = 480;
    private float worldMaxY = view.getHEIGHT()-160;

    private boolean isAttacking = false;

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
    private double velocityX = 0; //horizontalni rychlost


    // Úroveň země
    private double lastGroundY = 530;
    final double ATTACK_RANGE = 80;


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
        this(100, 500, 100, 100, new Inventory(), 1.0f, 0, 10, 0, 0, 100);
//        this.playerImage = new Image(getClass().getResourceAsStream("/images/player/player_idle_right.png"));
        this.currentDirection = Direction.RIGHT;
        this.playerImageLeft = new Image(getClass().getResourceAsStream("/images/player/player_idle_left.png"));
        this.playerImageRight = new Image(getClass().getResourceAsStream("/images/player/player_idle_right.png"));
        playerAttackLeft = new Image(getClass().getResourceAsStream("/images/player/player_attack_left.png"));
        playerAttackRight = new Image(getClass().getResourceAsStream("/images/player/player_attack_right.png"));
        playerImageJumpRight = new Image(getClass().getResourceAsStream("/images/player/player_jump_right.png"));
        playerImageJumpLeft = new Image(getClass().getResourceAsStream("/images/player/player_jump_left.png"));
        playerImageBlockingRight = new Image(getClass().getResourceAsStream("/images/player/player_blocking_right.png"));
        playerImageBlockingLeft = new Image(getClass().getResourceAsStream("/images/player/player_blocking_left.png"));


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

        boolean isMoving = movingLeft || movingRight || currentDirection == Direction.UP || currentDirection == Direction.DOWN;

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
        return getY();
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
        if (!onGround || isCrouching) return;

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

        double dx = 0;
        double dy = 0;

        double step = isCrouching ? 1.5 : (isSprinting ? 6 : 2.5);

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
    public void takeDamage(float amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }
    public void attack(List<Enemy> enemies) {

        if (isBlocking) {
            isBlocking = false;
        }

        isAttacking = true;

        new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.millis(200),
                        e -> isAttacking = false
                )
        ).play();

        for (Enemy enemy : enemies) {
            double dx = enemy.getX() - this.getX();
            double dy = enemy.getY() - this.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= ATTACK_RANGE) {
                enemy.takeDamage(this.getDamage());
                break;
            }
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

}

