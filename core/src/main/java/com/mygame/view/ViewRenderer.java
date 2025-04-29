package com.mygame.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.viewmodel.ViewModel;
import com.mygame.viewmodel.ViewModelManager;

/**
 * Класс для отрисовки игры (View в архитектуре MVVM)
 * Отвечает только за отрисовку данных, полученных от ViewModelManager
 */
public class ViewRenderer {
    private SpriteBatch batch;
    private ViewModelManager viewModelManager;
    private Viewport viewport;

    /**
     * Создание рендерера для отрисовки игры
     * @param batch менеджер спрайтов
     * @param viewModelManager менеджер моделей представления
     * @param viewport видовой экран
     */
    public ViewRenderer(SpriteBatch batch, ViewModelManager viewModelManager, Viewport viewport) {
        this.batch = batch;
        this.viewModelManager = viewModelManager;
        this.viewport = viewport;
    }

    /**
     * Отрисовка всех визуальных элементов игры
     */
    public void render() {
        // Отрисовка игровых объектов через SpriteBatch
        batch.begin();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Отрисовка всех моделей представления
        for (ViewModel viewModel : viewModelManager.getViewModels()) {
            viewModel.render(batch);
        }

        batch.end();

        // Отрисовка отладочной информации о физических телах (хитбоксах) поверх спрайтов
        viewModelManager.getDebugRenderer().render(
            viewModelManager.getPhysicsWorld(),
            viewport.getCamera().combined
        );
    }

    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        // ViewRenderer не владеет ресурсами, их освобождает ViewModelManager
        batch.dispose();
    }
}
