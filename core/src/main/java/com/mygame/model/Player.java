package com.mygame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

// Класс игрока, который наследует от GameObject
public class Player extends GameObject {

    // Конструктор для создания игрока
    public Player(World world, float x, float y) {
        super(world, x, y); // Вызов конструктора базового класса

        texture = new Texture("player.png"); // Загружаем текстуру для персонажа

        // Создание физического тела для игрока
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Тело будет динамическим
        bodyDef.position.set(x, y); // Начальная позиция

        body = world.createBody(bodyDef); // Создаем тело в мире

        // Создание формы для физического тела (круглая форма для игрока)
        CircleShape shape = new CircleShape();
        shape.setRadius(1); // Устанавливаем радиус персонажа

        // Настройки физического взаимодействия
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape; // Устанавливаем форму
        fixtureDef.density = 1f; // Плотность
        fixtureDef.friction = 0.5f; // Трение
        fixtureDef.restitution = 0.0f; // Упругость (0 = нет отскока)

        body.createFixture(fixtureDef); // Применяем настройки формы и физики
        shape.dispose(); // Освобождаем память, выделенную для формы

    }
    public void moveRight() {
        System.out.println("Applying force to the right!"); // Отладочный вывод
        // Применяем силу к телу игрока вправо
        body.applyForceToCenter(new Vector2(10, 0), true);
    }

    @Override
    public void update() {
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > 5) { // Ограничение скорости по X
            body.setLinearVelocity(5, velocity.y);
        }
        Vector2 position = body.getPosition();
        System.out.println("Player position: " + position);
        System.out.println("Player velocity: " + velocity);
    }
}
