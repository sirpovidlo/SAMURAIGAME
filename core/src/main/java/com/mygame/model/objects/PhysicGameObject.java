package com.mygame.model.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygame.model.GameModelContext;

public class PhysicGameObject implements GameObject{
    private final Body body;
    private final float maxSpeed;

    protected PhysicGameObject(Body body, float maxSpeed) {
        this.body = body;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void update(GameModelContext context) {
        updateLogic(context);

        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();

        // Limit max speed
        if (Math.abs(velocity.x) > maxSpeed)
        {
            body.setLinearVelocity(Math.signum(velocity.x)*maxSpeed, velocity.y);
        }

        // ограничение движения в пределах экрана
        float minX = context.getLeftBorderCoordinate();
        float maxX = context.getRightBorderCoordinate();

        if (position.x < minX)
        {
            body.setTransform(minX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        }
        else if (position.x > maxX)
        {
            body.setTransform(maxX, position.y, 0);
            body.setLinearVelocity(0, velocity.y);
        }

    }
    protected abstract void updateLogic(GameModelContext context);

    protected void applyForce(Vector2 force)
    {
        body.applyForceToCenter(force.x, force.y, true);
    }

    protected void setLinearVelocity(Vector2 velocity)
    {
        body.setLinearVelocity(velocity.x, velocity.y);
    }

    //получение физ тела игрока
    public Body getBody()
    {
        return body;
    }

    @Override
    public void dispose() {

    }
}
