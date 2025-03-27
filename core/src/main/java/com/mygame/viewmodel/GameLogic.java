package com.mygame.viewmodel;

import com.badlogic.gdx.math.Vector2;
import com.mygame.model.GameWorld;
import com.mygame.model.Player;

public class GameLogic {
    private GameWorld gameWorld;
    private boolean isJumpPressed;
    private boolean wasJumpPressed;

    public GameLogic(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.isJumpPressed = false;
        this.wasJumpPressed = false;
        gameWorld.setGameLogic(this);
    }

    public void update(float deltaTime) {
        Player player = (Player) gameWorld.getPlayer();
        updatePlayerState(player);
        gameWorld.update(deltaTime);
    }

    public void setJumpPressed(boolean pressed) {
        this.isJumpPressed = pressed;
    }

    public void handleLanding() {
        Player player = (Player) gameWorld.getPlayer();
        player.setGrounded(true);
        if (player.getCurrentState() == Player.PlayerState.JUMPING) {
            player.setState(Player.PlayerState.CROUCHING);
        }
    }

    private void updatePlayerState(Player player) {
        if (isJumpPressed && !wasJumpPressed && player.isGrounded()) {
            player.getBody().setLinearVelocity(
                player.getBody().getLinearVelocity().x,
                Player.JUMP_FORCE * 60  // Increased from 50 for higher jumps
            );
            player.setState(Player.PlayerState.JUMPING);
            player.setGrounded(false);
        }
        wasJumpPressed = isJumpPressed;

        Vector2 velocity = player.getBody().getLinearVelocity();

        if (player.isGrounded()) {
            if (Math.abs(velocity.x) > 0.1f) {
                player.setState(Player.PlayerState.STANDING);
            } else {
                player.setState(Player.PlayerState.STANDING);
            }
        } else {
            player.setState(Player.PlayerState.JUMPING);
        }
    }

    public void dispose() {
        gameWorld.dispose();
    }
}

