package com.mygame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.model.Player;

/**
 * Контроллер для управления игроком
 */
public class PlayerController implements Controller<Player> {
    @Override
    public Class<Player> getObjectType() {
        return Player.class;
    }

    @Override
    public void apply(Player controlledObject) {
        PlayerInput input = collectInput();
        controlledObject.setInput(input);
    }

    /**
     * Сбор текущего пользовательского ввода
     */
    private PlayerInput collectInput() {
        PlayerInput input = new PlayerInput();

        input.moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
        input.moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        input.jumpPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        return input;
    }
}
