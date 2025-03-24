package com.mygame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.Main;
import com.mygame.viewmodel.GameLogic;

public class GameWorld {
    private World world;
    private Body groundBody;
    private GameObject player;
    private GameLogic gameLogic;

    public GameWorld() {
        // Увеличиваем гравитацию для более быстрого падения
        world = new World(new Vector2(0, -50f), true);
        createGround();
        player = new Player(world, 0, 10);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("footSensor") ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals("footSensor")) {
                    if (gameLogic != null) {
                        gameLogic.handleLanding();
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("footSensor") ||
                    fixtureB.getUserData() != null && fixtureB.getUserData().equals("footSensor")) {
                    ((Player)player).setGrounded(false);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public World getWorld() {
        return world;
    }

    public GameObject getPlayer() {
        return player;
    }

    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);
        player.update();
    }

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

    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        groundBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Main.VIRTUAL_WIDTH, 33);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.7f;
        groundBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public Vector2 getGroundPosition() {
        return groundBody.getPosition();
    }
}

