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

    // Размеры виртуального экрана
    public static final float VIRTUAL_WIDTH = 800;
    public static final float VIRTUAL_HEIGHT = 600;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Создаем FitViewport с виртуальными размерами
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        gameWorld = new GameWorld();
        gameLogic = new GameLogic(gameWorld);
        gameRenderer = new GameRenderer(batch, gameWorld, viewport);
        inputHandler = new InputHandler();
    }

    @Override
    public void render() {
        // Очищаем экран с черным цветом
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        // Очищаем артефакты при ресайзе
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.update(width, height, true);

        // Центрируем камеру если нужно
        ((OrthographicCamera)viewport.getCamera()).position.set(
            viewport.getWorldWidth()/2,
            viewport.getWorldHeight()/2,
            0
        );
        viewport.getCamera().update();
    }

    // Остальные методы остаются без изменений
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
