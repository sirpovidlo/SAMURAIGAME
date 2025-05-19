package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.controller.PlayerController;
import com.mygame.controller.PlayerInput;
import com.mygame.model.GameModel;
import com.mygame.model.GameWorld;
import com.mygame.view.TextureManager;
import com.mygame.view.ViewRenderer;
import com.mygame.viewmodel.ViewModelManager;

/**
 * Основной класс игры
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameModel gameModel;
    private PlayerController playerController;
    private ViewModelManager viewModelManager;
    private ViewRenderer viewRenderer;
    private Viewport viewport;


    public static final float VIRTUAL_WIDTH = 40;
    public static final float VIRTUAL_HEIGHT = 30;

    @Override
    public void create() {
        // Инициализация основных компонентов
        batch = new SpriteBatch();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);


        // 1. Создание модели (Model в MVVM)
        gameModel = new GameModel();

        // 2. Создание контроллера для игрока (не входит в MVVM, является частью Controller в MVC)
        playerController = new PlayerController();


        // 3. Инициализируем TextureManager (часть View)
        // TextureManager инициализируется автоматически при первом обращении
        TextureManager.getInstance();

        // 4. Создание менеджера моделей представления (ViewModel в MVVM)
        viewModelManager = new ViewModelManager(gameModel, viewport);

        // 5. Создание рендерера (View в MVVM)
        viewRenderer = new ViewRenderer(batch, viewModelManager, viewport);
    }

    @Override
    public void render() {
        // Очистка экрана
        Gdx.gl.glClearColor(0, 0, 0, 1); // Черный цвет
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Применение вьюпорта
        viewport.apply();

        PlayerInput input = playerController.collectInput();

        gameModel.getPlayer().setInput(input);
        // 1. Обновление модели (бизнес-логика)
        gameModel.update(Gdx.graphics.getDeltaTime());

        // 2. Обновление моделей представления (подготовка данных для отображения)
        viewModelManager.update();

        // 3. Отрисовка (визуальное представление)
        viewRenderer.render();
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

    @Override
    public void dispose() {
        // Освобождение ресурсов в порядке, обратном их созданию
        viewRenderer.dispose();
        viewModelManager.dispose();

        // Освобождение текстур
        TextureManager.getInstance().dispose();

        gameModel.dispose();
        batch.dispose();
    }
}
