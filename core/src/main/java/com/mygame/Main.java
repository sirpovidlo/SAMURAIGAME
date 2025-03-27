package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameWorld;
import com.mygame.view.GameRenderer;
import com.mygame.viewmodel.GameLogic;
import com.mygame.viewmodel.InputHandler;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameWorld gameWorld;
    private GameLogic gameLogic;
    private InputHandler inputHandler;
    private GameRenderer gameRenderer;
    private Viewport viewport;

    public static final float VIRTUAL_WIDTH = 800;
    public static final float VIRTUAL_HEIGHT = 600;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        gameWorld = new GameWorld();
        gameLogic = new GameLogic(gameWorld);
        inputHandler = new InputHandler(gameLogic);
        gameRenderer = new GameRenderer(batch, gameWorld, viewport);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.update(width, height, true);

        ((OrthographicCamera)viewport.getCamera()).position.set(
            viewport.getWorldWidth()/2,
            viewport.getWorldHeight()/2,
            0
        );
        viewport.getCamera().update();
    }

    private void input() {
        inputHandler.handleInput(gameWorld.getPlayer());
    }

    private void logic() {
        gameLogic.update(Gdx.graphics.getDeltaTime());
    }

    private void draw() {
        gameRenderer.render();
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
        gameLogic.dispose();
        gameWorld.dispose();
        batch.dispose();
    }
}
