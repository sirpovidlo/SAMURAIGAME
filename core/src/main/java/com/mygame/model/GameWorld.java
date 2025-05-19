package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.Main;
import com.mygame.controller.PlayerController;


/**
 * Класс, отвечающий за физический мир и столкновения
 */
public class GameWorld {
    private World world;
    private Body groundBody;
    private Player player;

    // Константа масштабирования между физикой (метры) и рендерингом (пиксели)
    public static final float PPM = 16.0f;

    // Константы для физического движка
    private static final int VELOCITY_ITERATIONS = 12;
    private static final int POSITION_ITERATIONS = 6;

    /**
     * Создание физического мира
     */
    public GameWorld() {
        // Инициализация физического мира с гравитацией (в метрах, а не в пикселях)
        world = new World(new Vector2(0, -20f), true);
        // Настройка параметров мира для улучшения физики
        createGround();
        setupContactListener();
    }

    /**
     * Установка игрока для мира
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Настройка обработчика столкновений
     */
    private void setupContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("footSensor") ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals("footSensor")) {
                    if (player != null) {
                        player.handleLanding();
                    }

                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("footSensor") ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals("footSensor")) {
                    if (player != null) {
                        player.setGrounded(false);
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    /**
     * Получение физического мира Box2D
     */
    public World getWorld() {
        return world;
    }

    /**
     * Обновление физического мира
     */
    public void update(float deltaTime) {
        // Используем фактический deltaTime для синхронизации с отрисовкой
        // Ограничиваем deltaTime, чтобы избежать туннельного эффекта
        float clampedDeltaTime = Math.min(deltaTime, 0.016f); // макс. 1/60 сек для более стабильной физики

        // Выполняем симуляцию с улучшенными параметрами
        world.step(clampedDeltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        if (world != null) {
            world.dispose();
            world = null;
        }
    }

    /**
     * Создание физического тела игрока
     */
    public Body createPlayerBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Все сразу в метрах
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.1f;
        bodyDef.bullet = true; // предотвращение сквозных пролётов

        Body body = world.createBody(bodyDef);

        // Размеры игрока
        float widthMetr = 3.75f;
        float heightMetr = 5.645f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widthMetr / 2, heightMetr / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;

        // Создание основного тела
        body.createFixture(fixtureDef);
        shape.dispose();

        // Сенсор ног (для проверки касания земли)
        PolygonShape sensorShape = new PolygonShape();
        float sensorWidth = 3.5f;   // чуть уже тела
        float sensorHeight = 0.5f;   // тонкий сенсор
        float sensorOffsetY = -heightMetr / 2; // смещён вниз от центра тела

        sensorShape.setAsBox(sensorWidth / 2, sensorHeight / 2,
            new Vector2(0, sensorOffsetY), 0);

        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;

        body.createFixture(sensorDef).setUserData("footSensor");
        sensorShape.dispose();

        return body;
    }

    /**
     * Создание земли
     */
    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Размещаем землю в нижней части экрана
        bodyDef.position.set((Main.VIRTUAL_WIDTH) / 2, 1);

        groundBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((Main.VIRTUAL_WIDTH ) / 2, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.7f;
        groundBody.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Получение позиции земли
     */
    public Vector2 getGroundPosition() {
        return groundBody.getPosition();
    }
}
