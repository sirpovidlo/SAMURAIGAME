package com.mygame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameWorld {
    private World world; // Физический мир Box2D
    private Body groundBody; // Физическое тело земли
    private GameObject player; // Игрок

    public GameWorld() {
        // Создаем физический мир с гравитацией (0, -9.8)
        world = new World(new Vector2(0, -9.8f), true);

        createGround(); // Создаем землю
        player = new Player(world, 0, 10); // Создаем игрока в начальной позиции (0, 10)
    }

    public World getWorld() {
        return world;
    }

    public GameObject getPlayer() {
        return player;
    }

    // Обновление мира (физические расчеты)
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2); // Итерации для точности симуляции
        player.update(); // Обновление состояния игрока
    }

    // Освобождение ресурсов
    public void dispose() {
        if (player != null) {
            player.dispose();
            player = null;
        }

        if (world != null) {
            world.dispose();
            world = null;
        }
    }

    // Метод для создания земли в мире Box2D
    private void createGround() {
        float screenWidth = Gdx.graphics.getWidth();  // Получаем ширину экрана
        float screenHeight = Gdx.graphics.getHeight(); // Получаем высоту экрана

        // Создаем статическое тело для пола
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);  // Позиция пола по центру экрана по оси X и внизу экрана по Y

        groundBody = world.createBody(bodyDef);

        // Устанавливаем размеры пола по экрану
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(screenWidth , 33);  // Пол занимает всю ширину экрана, высота 1

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.7f;  // Можно настроить трение пола
        groundBody.createFixture(fixtureDef);
        shape.dispose();
    }



    public Vector2 getGroundPosition() {
        return groundBody.getPosition(); // Получаем позицию земли
    }
}
