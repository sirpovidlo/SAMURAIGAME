package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygame.Main;

/**
 * Класс, объединяющий игровую логику и физический движок
 */
public class GameModel {
    private final GameWorld gameWorld;
    private Player player;

    public GameModel() {
        gameWorld = new GameWorld();
        initialize();
    }

    /**
     * Инициализация игровой модели
     */
    public void initialize() {
        // Позиция задается в пикселях, преобразование в метры происходит в createPlayerBody
        player = new Player(gameWorld.createPlayerBody(60, 150));
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public World getPhysicsWorld() {
        return gameWorld.getWorld();
    }

    public Player getPlayer() {
        return player;
    }

    public void update(float deltaTime) {
        // Сначала обновляем физику мира с ограничением на шаг времени
        gameWorld.update(deltaTime);

        // Затем обновляем все игровые объекты с новыми физическими данными
        player.update();
    }

    public void dispose() {
        if (player != null) {
            player.dispose();
        }
        gameWorld.dispose();
    }

    public Vector2 getGroundPosition() {
        return gameWorld.getGroundPosition();
    }
}
