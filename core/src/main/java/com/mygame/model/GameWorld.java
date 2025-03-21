package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

// Класс игрового мира
public class GameWorld {
    private World world; // Физический мир
    private Body groundBody; // Тело для пола
    private GameObject player;  // Игрок

    // Конструктор для создания игрового мира
    public GameWorld() {
        world = new World(new Vector2(0, -1000f), true); // Создаем физический мир с гравитацией

        createBackground(); // Создание фона
        createGround();     // Создание пола

        player = new Player(world, 0, 100); // Создание игрока в мире
    }

    public World getWorld() {
        return world; // Возвращаем физический мир
    }

    public GameObject getPlayer() {
        return player; // Возвращаем игрока
    }

    // Метод для обновления мира
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2); // Шаг симуляции мира
        player.update(); // Обновление состояния игрока
    }

    // Освобождение ресурсов
    public void dispose() {
        world.dispose(); // Освобождаем мир
        player.dispose(); // Освобождаем ресурсы игрока
    }

    // Создание фона
    private void createBackground() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Фон — статическое тело
        bodyDef.position.set(400, 300); // Позиция фона

        Body backgroundBody = world.createBody(bodyDef); // Создаем тело для фона
        PolygonShape shape = new PolygonShape(); // Прямоугольная форма фона
        shape.setAsBox(400, 300); // Устанавливаем размеры фона

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Устанавливаем форму
        backgroundBody.createFixture(fixtureDef); // Применяем физику к фону
        shape.dispose(); // Освобождаем форму
    }

    // Создание пола
    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Пол — статическое тело
        bodyDef.position.set(0, 0); // Позиция пола

        groundBody = world.createBody(bodyDef); // Создаем тело для пола
        PolygonShape shape = new PolygonShape(); // Прямоугольная форма пола
        shape.setAsBox(400, 50); // Устанавливаем размеры пола

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Устанавливаем форму
        groundBody.createFixture(fixtureDef); // Применяем физику к полу
        shape.dispose(); // Освобождаем форму
    }

    public Vector2 getGroundPosition() {
        return groundBody.getPosition(); // Возвращаем позицию пола
    }
}
