package com.mygame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.Main;

public class Player extends GameObject {
    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 500f;
    public static final float DECELERATION = 0.8f;
    public static final float JUMP_FORCE = 20f;

    public enum PlayerState {
        STANDING,
        CROUCHING,
        JUMPING
    }

    private PlayerState currentState;
    private Texture standingTexture;
    private Texture crouchingTexture;
    private Texture jumpingTexture;
    private boolean isGrounded;
    private boolean facingRight = true;

    public Player(World world, float x, float y) {
        super(world, x, y);

        standingTexture = new Texture("player.png");
        crouchingTexture = new Texture("player_crouch.png");
        jumpingTexture = new Texture("player_jump.png");
        texture = standingTexture;
        currentState = PlayerState.STANDING;
        isGrounded = true;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.2f; // Add damping for smoother movement

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20f, 30f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.1f;

        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(19f, 2f, new Vector2(0, -30f), 0);

        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.createFixture(sensorDef).setUserData("footSensor");

        shape.dispose();
        sensorShape.dispose();
    }

    public void accelerateRight() {
        Vector2 velocity = body.getLinearVelocity();

        // Only accelerate if below max speed
        if (velocity.x < MAX_SPEED) {
            body.applyForceToCenter(ACCELERATION * body.getMass(), 0, true);
        }

        facingRight = true;
    }

    public void accelerateLeft() {
        Vector2 velocity = body.getLinearVelocity();

        // Only accelerate if below max speed
        if (velocity.x > -MAX_SPEED) {
            body.applyForceToCenter(-ACCELERATION * body.getMass(), 0, true);
        }

        facingRight = false;
    }

    public void decelerate() {
        Vector2 velocity = body.getLinearVelocity();

        // Apply deceleration force in opposite direction of movement
        if (Math.abs(velocity.x) > 0.1f) {
            float decelerationForce = -Math.signum(velocity.x) * DECELERATION * body.getMass();
            body.applyForceToCenter(decelerationForce, 0, true);
        } else {
            // If very slow, just stop completely
            body.setLinearVelocity(0, velocity.y);
        }
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setState(PlayerState state) {
        this.currentState = state;
        switch (state) {
            case STANDING:
                texture = standingTexture;
                break;
            case CROUCHING:
                texture = crouchingTexture;
                break;
            case JUMPING:
                texture = jumpingTexture;
                break;
        }
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    @Override
    public void update() {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        // Limit maximum speed
        if (Math.abs(velocity.x) > MAX_SPEED) {
            body.setLinearVelocity(Math.signum(velocity.x) * MAX_SPEED, velocity.y);
        }

        float minX = 0 + 20;
        float maxX = Main.VIRTUAL_WIDTH - 20;

        if (position.x < minX) {
            body.setTransform(minX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        } else if (position.x > maxX) {
            body.setTransform(maxX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        }
    }

    @Override
    public void dispose() {
        if (standingTexture != null) {
            standingTexture.dispose();
        }
        if (crouchingTexture != null) {
            crouchingTexture.dispose();
        }
        if (jumpingTexture != null) {
            jumpingTexture.dispose();
        }
        body = null;
    }
}

