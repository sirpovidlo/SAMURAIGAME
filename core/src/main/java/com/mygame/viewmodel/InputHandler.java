package com.mygame.viewmodel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygame.model.GameObject;
import com.mygame.model.Player;

public class InputHandler {
    private GameLogic gameLogic;

    public InputHandler(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void handleInput(GameObject gameObject) {
        if (gameObject instanceof Player) {
            Player player = (Player) gameObject;

            boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D);
            boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
            boolean jumping = Gdx.input.isKeyPressed(Input.Keys.SPACE);
            boolean sprinting = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

            // Handle jump through GameLogic
            gameLogic.setJumpPressed(jumping);

            // Apply acceleration-based movement
            if (movingRight) {
                player.accelerateRight();
            } else if (movingLeft) {
                player.accelerateLeft();
            } else {
                // If no movement keys are pressed, decelerate
                player.decelerate();
            }
        }
    }

    public void dispose() {
        // Clean up resources if needed
    }
}

