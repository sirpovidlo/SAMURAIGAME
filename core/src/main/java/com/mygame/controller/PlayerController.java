package com.mygame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygame.model.Player;

/**
 * Контроллер для управления игроком
 */
public class PlayerController {
    private boolean isJumpPressed;
    private boolean wasJumpPressed;
    private Player player;

    /**
     * Конструктор контроллера с привязкой к игроку
     */
    public PlayerController(Player player) {
        this.player = player;
        this.isJumpPressed = false;
        this.wasJumpPressed = false;
        
        // Устанавливаем контроллер для игрока
        player.setController(this);
    }

    /**
     * Обработка входных данных и применение к игроку
     */
    public void applyControl(Player player) {
        // Проверка нажатия клавиш
        boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean jumping = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        // Обработка прыжка
        isJumpPressed = jumping;
        
        // Обработка движения
        if (movingRight) {
            player.accelerateRight();
        } else if (movingLeft) {
            player.accelerateLeft();
        } else {
            player.decelerate();
        }
        
        // Обновление состояния прыжка
        updatePlayerState(player);
    }

    /**
     * Обновление состояния игрока
     */
    private void updatePlayerState(Player player) {
        if (isJumpPressed && !wasJumpPressed && player.isGrounded()) {
            player.getBody().setLinearVelocity(
                player.getBody().getLinearVelocity().x,
                Player.JUMP_FORCE * 6
            );
            player.setState(Player.PlayerState.JUMPING);
            player.setGrounded(false);
        }
        wasJumpPressed = isJumpPressed;

        Vector2 velocity = player.getBody().getLinearVelocity();

        if (player.isGrounded()) {
            if (Math.abs(velocity.x) > 0.1f) {
                player.setState(Player.PlayerState.STANDING);
            } else {
                player.setState(Player.PlayerState.STANDING);
            }
        } else {
            player.setState(Player.PlayerState.JUMPING);
        }
    }

    /**
     * Обработка приземления игрока
     */
    public void handleLanding() {
        player.setGrounded(true);
        if (player.getCurrentState() == Player.PlayerState.JUMPING) {
            player.setState(Player.PlayerState.CROUCHING);
        }
    }

    /**
     * Установка состояния "на земле"
     */
    public void setPlayerGrounded(boolean grounded) {
        player.setGrounded(grounded);
    }
} 