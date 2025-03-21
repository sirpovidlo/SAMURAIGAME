package com.mygame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.model.entities.Hero;
import com.mygame.model.world.GameWorld;

/**
 * Контроллер игры, обрабатывающий пользовательский ввод.
 * Взаимодействует только с Model слоем.
 */
public class GameController {
    private final GameWorld gameWorld;
    private final Hero hero;
    private final float moveSpeed = 4f;

    public GameController(GameWorld gameWorld, Hero hero) {
        this.gameWorld = gameWorld;
        this.hero = hero;
    }

    public void handleInput() {
        float delta = gameWorld.getDeltaTime();

        // Движение влево/вправо
        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            hero.moveRight(delta, moveSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            hero.moveLeft(delta, moveSpeed);
        }

        // Прыжок
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            hero.jump();
        }

        // Приседание
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            hero.squat();
        } else if (hero.getState() == Hero.HeroState.SQUAT) {
            hero.standUp();
        }
    }
}
