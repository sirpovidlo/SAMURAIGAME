package com.mygame.view;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameWorld;
import com.mygame.model.Player;
import com.badlogic.gdx.Gdx;

public class GameRenderer {
    private SpriteBatch batch;
    private Texture groundTexture;
    private Texture backgroundImage;
    private GameWorld gameWorld;
    private Box2DDebugRenderer debugRenderer;
    private Viewport viewport;

    public GameRenderer(SpriteBatch batch, GameWorld gameWorld, Viewport viewport) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        this.viewport = viewport;

        backgroundImage = new Texture("background.jpg");
        groundTexture = new Texture("groud1.png");
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render() {
        // Clear frame buffer
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Set projection matrix from viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        // Draw background
        batch.draw(backgroundImage,
            0, 0,
            viewport.getWorldWidth(), viewport.getWorldHeight());

        // Draw ground
        Vector2 groundPos = gameWorld.getGroundPosition();
        batch.draw(groundTexture,
            groundPos.x - viewport.getWorldWidth()/2, groundPos.y - 1,
            viewport.getWorldWidth(), 2);

        // Draw player with correct orientation based on movement direction
        Vector2 playerPos = gameWorld.getPlayer().getBody().getPosition();
        float width = 40f;
        float height = 60f;

        Player player = (Player)gameWorld.getPlayer();

        // Flip the texture based on facing direction
        if (player.isFacingRight()) {
            batch.draw(player.getTexture(),
                playerPos.x - width/2, playerPos.y - height/2,
                width, height);
        } else {
            // Draw flipped when facing left
            batch.draw(player.getTexture(),
                playerPos.x + width/2, playerPos.y - height/2,
                -width, height);
        }

        batch.end();

        // Draw hitboxes
        debugRenderer.render(gameWorld.getWorld(), viewport.getCamera().combined);
    }

    public void dispose() {
        groundTexture.dispose();
        backgroundImage.dispose();
        debugRenderer.dispose();
    }
}

