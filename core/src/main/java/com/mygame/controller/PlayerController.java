package com.mygame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.model.Player;

/**
 * Контроллер для управления игроком
 */
public class PlayerController {
    private boolean isJumpPressed;
    private boolean wasJumpPressed;

    /**
     * Конструктор контроллера с привязкой к игроку
     */
//    public PlayerController(Player player) {
//        this.isJumpPressed = false;
//        this.wasJumpPressed = false;
//
//        // Устанавливаем контроллер для игрока
//        player.setController(this);
//    }
//
//    /**
//     * Обработка входных данных и применение к игроку
//     */
//    public void applyControl(Player player) {
//        // Проверка нажатия клавиш
//        boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D);
//        boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
//        boolean jumping = Gdx.input.isKeyPressed(Input.Keys.SPACE);
//
//        // Обработка прыжка
//        isJumpPressed = jumping;
//
//        // Обработка движения
//        if (movingRight) {
//            player.move(Player.Direction.RIGHT);
//        } else if (movingLeft) {
//            player.move(Player.Direction.LEFT);
//        } else {
//            player.move(Player.Direction.NONE);
//        }
//
//        // Обработка прыжка
//        if (isJumpPressed && !wasJumpPressed && player.isGrounded()) {
//            player.jump();
//        }
//        wasJumpPressed = isJumpPressed;
//    }
    public PlayerInput collectInput() {
        PlayerInput input = new PlayerInput();

        input.moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
        input.moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        input.jumpPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        return input;
    }
}
