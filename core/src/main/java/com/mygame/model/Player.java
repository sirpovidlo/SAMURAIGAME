package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.Main;
import com.mygame.controller.PlayerController;

/**
 * Класс игрока
 */
public class Player implements GameObject {
    // Константы управления (масштабированы для физического мира)
    public static final float MAX_SPEED = 15.0f;
    public static final float ACCELERATION = 25.0f;
    public static final float DECELERATION = 10.8f;
    public static final float JUMP_FORCE = 4.0f;

    public enum PlayerState {
        STANDING,
        CROUCHING,
        JUMPING
    }

    private PlayerState currentState;
    private Body body;
    private boolean isGrounded;
    private boolean facingRight = true;
    private PlayerController controller;

    /**
     * Конструктор игрока, принимающий готовое физическое тело
     */
    public Player(Body body) {
        this.body = body;

        currentState = PlayerState.STANDING;
        isGrounded = true;
    }

    /**
     * Установка контроллера игрока
     */
    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    /**
     * Получение физического тела игрока
     */
    public Body getBody() {
        return body;
    }

    /**
     * Ускорение вправо
     */
    public void accelerateRight() {
        Vector2 velocity = body.getLinearVelocity();

        // Only accelerate if below max speed
        if (velocity.x < MAX_SPEED) {
            body.applyForceToCenter(ACCELERATION * body.getMass(), 0, true);
        }

        facingRight = true;
    }

    /**
     * Ускорение влево
     */
    public void accelerateLeft() {
        Vector2 velocity = body.getLinearVelocity();

        // Only accelerate if below max speed
        if (velocity.x > -MAX_SPEED) {
            body.applyForceToCenter(-ACCELERATION * body.getMass(), 0, true);
        }

        facingRight = false;
    }

    /**
     * Замедление
     */
    public void decelerate() {
        Vector2 velocity = body.getLinearVelocity();

        // Apply deceleration force in opposite direction of movement
        if (Math.abs(velocity.x) > 0.1f) {
            float decelerationForce = -Math.signum(velocity.x) * DECELERATION * body.getMass();
            body.applyForceToCenter(decelerationForce, 0, true);
        } else {
            // If very slow, just stop completely
            body.setLinearVelocity(0, velocity.y);
        }
    }

    /**
     * Проверка направления движения
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Получение текущего состояния игрока
     */
    public PlayerState getCurrentState() {
        return currentState;
    }

    /**
     * Установка состояния игрока
     */
    public void setState(PlayerState state) {
        this.currentState = state;
    }

    /**
     * Проверка, находится ли игрок на земле
     */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Установка состояния "на земле"
     */
    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    /**
     * Обновление состояния игрока
     */
    @Override
    public void update() {
        // Применение управления к игроку
        if (controller != null) {
            controller.applyControl(this);
        }

        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        // Limit maximum speed
        if (Math.abs(velocity.x) > MAX_SPEED) {
            body.setLinearVelocity(Math.signum(velocity.x) * MAX_SPEED, velocity.y);
        }

        // Ограничение движения в пределах экрана
        float minX = (0 + 60) / GameWorld.PPM; // (левая граница)
        float maxX = (Main.VIRTUAL_WIDTH - 220) / GameWorld.PPM; // (правая граница)

        if (position.x < minX) {
            body.setTransform(minX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        } else if (position.x > maxX) {
            body.setTransform(maxX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        }
    }

    /**
     * Освобождение ресурсов
     */
    @Override
    public void dispose() {
        body = null;
    }
}
