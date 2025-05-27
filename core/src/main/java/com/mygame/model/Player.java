package com.mygame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygame.controller.PlayerInput;

/**
 * Класс игрока
 */
public class Player implements GameObject {
    private final String id;
    private final PlayerPhysics physics;
    private PlayerState currentState;
    private boolean isGrounded;
    private boolean facingRight;
    private PlayerInput currentInput;
    private boolean wasJumpPressed;

    // Размеры коллайдера игрока
    private final float width;
    private final float height;

    // Время для смены состояния (в миллисекундах)
    private long stateChangeTimeMillis = -1;
    // Константа для задержки перехода из приседания в стойку (в секундах)
    private static final float CROUCH_DELAY = 0.2f;

    public enum Direction {
        LEFT, RIGHT, NONE
    }

    public enum PlayerState {
        STANDING,
        CROUCHING,
        JUMPING
    }

    /**
     * Конструктор игрока
     */
    public Player(Body body, float width, float height) {
        this.id = "player"; // Для игрока можно использовать фиксированный id
        this.physics = new PlayerPhysics(body);
        this.currentState = PlayerState.STANDING;
        this.isGrounded = true;
        this.facingRight = true;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Получить ширину игрока
     */
    public float getWidth() {
        return width;
    }

    /**
     * Получить высоту игрока
     */
    public float getHeight() {
        return height;
    }

    /**
     * Установка входных данных от контроллера
     */
    public void setInput(PlayerInput input) {
        this.currentInput = input;
        processInput();
    }

    /**
     * Обработка входных данных
     */
    private void processInput() {
        // Обработка движения
        if (currentInput.moveRight) {
            move(Direction.RIGHT);
        } else if (currentInput.moveLeft) {
            move(Direction.LEFT);
        } else {
            move(Direction.NONE);
        }

        // Обработка прыжка
        if (currentInput.jumpPressed && !wasJumpPressed && isGrounded) {
            jump();
        }
        wasJumpPressed = currentInput.jumpPressed;
    }

    /**
     * Перемещение игрока в указанном направлении
     */
    public void move(Direction direction) {
        physics.move(direction);

        // Обновление направления взгляда
        if (direction == Direction.RIGHT) {
            facingRight = true;
        } else if (direction == Direction.LEFT) {
            facingRight = false;
        }
    }

    /**
     * Прыжок игрока
     */
    public void jump() {
        if (isGrounded) {
            physics.applyJumpForce();

            if (currentState == PlayerState.CROUCHING) {
                stateChangeTimeMillis = -1;
            }

            setState(PlayerState.JUMPING);
            setGrounded(false);
        }
    }

    @Override
    public void update(ModelContext context) {
        // Обновление физики
        physics.update(context);

        // Проверка необходимости смены состояния
        if (stateChangeTimeMillis > 0 && TimeUtils.millis() >= stateChangeTimeMillis) {

            if (currentState == PlayerState.CROUCHING) {
                setState(PlayerState.STANDING);
            }
            stateChangeTimeMillis = -1;
        }
    }

    /**
     * Обработка приземления
     */
    public void handleLanding() {
        setGrounded(true);
        if (getCurrentState() == PlayerState.JUMPING) {
            setState(PlayerState.CROUCHING);
            // Устанавливаем время для смены состояния
            stateChangeTimeMillis = TimeUtils.millis() + (long)(CROUCH_DELAY * 1000);
        }
    }

    // Геттеры
    public Vector2 getPosition() {
        return physics.getPosition();
    }

    public Vector2 getVelocity() {
        return physics.getVelocity();
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public void setState(PlayerState state) {
        this.currentState = state;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    @Override
    public void dispose() {
        physics.dispose();
    }
}
