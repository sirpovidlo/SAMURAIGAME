package com.mygame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.Main;

public class Player extends GameObject {
    private static final float MOVE_SPEED = 500f; // Фиксированная скорость движения
    private static final float PLAYER_RADIUS = 3f; // Радиус хитбокса
    private static final float MAX_SPEED = 5f; // Максимальная скорость

    public Player(World world, float x, float y) {
        super(world, x, y);
        texture = new Texture("player.png");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;


        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20f, 30f); // Половина ширины = 2, половина высоты = 3


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 1f;
        //fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void stopMovement() {
        // Устанавливаем скорость по оси X равной 0
        Vector2 velocity = body.getLinearVelocity();
        body.setLinearVelocity(0, velocity.y); // Устанавливаем X-скорость в 0, оставляя Y-скорость
    }

    public void moveRight() {
        body.setLinearVelocity(MOVE_SPEED, body.getLinearVelocity().y); // Устанавливаем скорость
    }

    public void moveLeft() {
        body.setLinearVelocity(-MOVE_SPEED, body.getLinearVelocity().y); // Устанавливаем скорость
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y); // Остановка по X
    }

    @Override
    public void update() {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        // Используем виртуальные границы из Main
        float minX = 0 + 20;
        float maxX = Main.VIRTUAL_WIDTH - 20;

        if (position.x < minX) {
            body.setTransform(minX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        } else if (position.x > maxX) {
            body.setTransform(maxX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        }

        if (Math.abs(velocity.x) > MAX_SPEED) {
            body.setLinearVelocity(Math.signum(velocity.x) * MAX_SPEED, velocity.y);
        }
    }



    private boolean isMoving() {
        return Math.abs(body.getLinearVelocity().x) > 0.1f;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
        body = null; // Освобождаем тело
    }
}
