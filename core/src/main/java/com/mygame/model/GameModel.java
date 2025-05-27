package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygame.controller.ControllersManager;
import com.mygame.controller.Controller;
import com.mygame.events.GameEventBus;
import com.mygame.events.GameEventType;
import com.mygame.events.GameObjectEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, объединяющий игровую логику и физический движок
 */
public class GameModel {
    private final GameWorld gameWorld;
    private Player player;
    private final ControllersManager controllersManager;
    private final List<GameObject> gameObjects;
    private final ModelContext modelContext;

    public GameModel(List<Controller<? extends GameObject>> controllers) {
        gameWorld = new GameWorld();
        gameObjects = new ArrayList<>();
        controllersManager = new ControllersManager(controllers);
        modelContext = new ModelContext(this);
        initialize();
    }

    /**
     * Добавление игрового объекта
     */
    public void addGameObject(GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
            GameEventBus.getInstance().publish(
                new GameObjectEvent(GameEventType.OBJECT_CREATED, gameObject)
            );
        }
    }

    /**
     * Удаление игрового объекта
     */
    public void removeGameObject(GameObject gameObject) {
        if (gameObjects.remove(gameObject)) {
            GameEventBus.getInstance().publish(
                new GameObjectEvent(GameEventType.OBJECT_DELETED, gameObject)
            );
            gameObject.dispose();
        }
    }

    /**
     * Инициализация игровой модели
     */
    private void initialize() {
        Object[] playerData = gameWorld.createPlayerBody(GameWorld.PLAYER_START_X, GameWorld.PLAYER_START_Y);
        Body body = (Body) playerData[0];
        float width = (float) playerData[1];
        float height = (float) playerData[2];

        player = new Player(body, width, height);
        gameWorld.setPlayer(player);
        addGameObject(player);
    }

    public World getPhysicsWorld() {
        return gameWorld.getWorld();
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Обновление состояния модели
     */
    public void update(float deltaTime) {
        // 1. Применение ввода к игровым объектам
        controllersManager.applyInput(gameObjects);

        // 2. Обновление игровой логики для каждого объекта
        for (GameObject object : gameObjects) {
            object.update(modelContext);
        }

        // 3. Обновление физического движка
        gameWorld.update(deltaTime);
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
