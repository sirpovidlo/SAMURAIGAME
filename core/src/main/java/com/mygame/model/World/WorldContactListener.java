package com.mygame.model.World;

import com.badlogic.gdx.physics.box2d.*;
import com.mygame.controller.PlayerController;

public class WorldContactListener implements ContactListener
{
    private final PlayerController playerController;

    WorldContactListener(PlayerController playerController)
    {
        this.playerController = playerController;
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null && fixtureB.getUserData().equals("footSensor") ||
            fixtureA.getUserData() != null && fixtureB.getUserData().equals("footSensor"))
        {
            if(playerController != null)
            {
                playerController.handleLanding();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("footSensor") ||
            fixtureB.getUserData() != null && fixtureB.getUserData().equals("footSensor")) {
            if (playerController != null) {
                playerController.setPlayerGrounded(false);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
