package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Создание и передача GameWorld в GameLogic и GameRenderer
        gameWorld = new GameWorld();
        gameLogic = new GameLogic(gameWorld);
        gameRenderer = new GameRenderer(batch, gameWorld);

        inputHandler = new InputHandler();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        inputHandler.handleInput(gameWorld.getPlayer()); // Передаем GameObject
    }

    private void logic() {
        gameLogic.update(Gdx.graphics.getDeltaTime());
    }

    private void draw() {
        gameRenderer.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameLogic.dispose();
        gameRenderer.dispose();
        gameWorld.dispose();  // Освобождение ресурсов мира
    }
}
