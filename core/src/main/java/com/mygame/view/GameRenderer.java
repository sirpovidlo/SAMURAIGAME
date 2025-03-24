package com.mygame.view;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameWorld;
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
        // Очистка буфера кадра
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Устанавливаем проекционную матрицу из viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        // Рисуем фон на весь виртуальный экран
        // Рисуем фон (теперь с правильным масштабированием)
        batch.draw(backgroundImage,
            0, 0,
            viewport.getWorldWidth(), viewport.getWorldHeight());
        // Рисуем землю
        Vector2 groundPos = gameWorld.getGroundPosition();
        batch.draw(groundTexture,
            groundPos.x - viewport.getWorldWidth()/2, groundPos.y - 1,
            viewport.getWorldWidth(), 2);

        // Рисуем игрока
        Vector2 playerPos = gameWorld.getPlayer().getBody().getPosition();
        float width = 40f;
        float height = 60f;
        batch.draw(gameWorld.getPlayer().getTexture(),
            playerPos.x - width/2, playerPos.y - height/2,
            width, height);

        batch.end();

        // Рисуем хитбоксы (используем матрицу из viewport)
        debugRenderer.render(gameWorld.getWorld(), viewport.getCamera().combined);
    }

    public void dispose() {
        groundTexture.dispose();
        backgroundImage.dispose();
        debugRenderer.dispose();
    }
}
