package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Класс, отвечающий за физический мир и столкновения
 */
public class GameWorld {
    // Размеры игрового мира
    public static final float WORLD_WIDTH = 40.0f;
    public static final float WORLD_HEIGHT = 30.0f;

    // Константы для размеров игрока
    public static final float PLAYER_WIDTH = 3.75f;
    public static final float PLAYER_HEIGHT = 5.645f;
    public static final float PLAYER_SENSOR_WIDTH = 3.5f;
    public static final float PLAYER_SENSOR_HEIGHT = 0.5f;
    public static final float PLAYER_START_X = 1.0f;
    public static final float PLAYER_START_Y = 5.0f;

    // Константы для земли
    public static final float GROUND_HEIGHT = 1.0f;
    public static final float GROUND_Y = GROUND_HEIGHT;

    private World world;
    private Body groundBody;
    private Player player;

    // Константы для физического движка
    private static final int VELOCITY_ITERATIONS = 12;
    private static final int POSITION_ITERATIONS = 6;

    /**
     * Создание физического мира
     */
    public GameWorld() {
        // Инициализация физического мира с гравитацией
        world = new World(new Vector2(0, -20f), true);
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
        float clampedDeltaTime = Math.min(deltaTime, 0.016f);
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
     * @return Массив объектов: [0] - тело (Body), [1] - ширина (float), [2] - высота (float)
     */
    public Object[] createPlayerBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.1f;
        bodyDef.bullet = true;

        Body body = world.createBody(bodyDef);

        // Создание основного тела
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PLAYER_WIDTH / 2, PLAYER_HEIGHT / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Сенсор ног
        PolygonShape sensorShape = new PolygonShape();
        float sensorOffsetY = -PLAYER_HEIGHT / 2;

        sensorShape.setAsBox(
            PLAYER_SENSOR_WIDTH / 2,
            PLAYER_SENSOR_HEIGHT / 2,
            new Vector2(0, sensorOffsetY),
            0
        );

        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;

        body.createFixture(sensorDef).setUserData("footSensor");
        sensorShape.dispose();

        return new Object[] { body, PLAYER_WIDTH, PLAYER_HEIGHT };
    }

    /**
     * Создание земли
     */
    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(WORLD_WIDTH / 2, GROUND_Y);

        groundBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WORLD_WIDTH / 2, GROUND_HEIGHT);

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
