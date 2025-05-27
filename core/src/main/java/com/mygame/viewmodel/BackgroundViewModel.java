package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameObject;
import com.mygame.view.TextureManager;

/**
 * Модель представления для фона
 * Преобразует данные модели в формат, удобный для отображения
 */
public class BackgroundViewModel implements ViewModel {
    private static final String BACKGROUND_ID = "background_id";
    private Viewport viewport;

    // Данные для отрисовки
    private float width;
    private float height;
    private float x;
    private float y;

    // Ссылка на текстуру из TextureManager
    private String textureId = TextureManager.BACKGROUND;

    /**
     * Создание модели представления для фона
     * @param viewport видовой экран для определения размеров
     */
    public BackgroundViewModel(Viewport viewport) {
        this.viewport = viewport;
        updateViewportData(viewport);
    }

    @Override
    public String getGameObjectId() {
        return BACKGROUND_ID;
    }

    /**
     * Обновление данных из viewport
     */
    private void updateViewportData(Viewport viewport) {
        width = viewport.getWorldWidth();
        height = viewport.getWorldHeight();
        x = 0;
        y = 0;
    }

    /**
     * Обновление данных представления
     */
    @Override
    public void update(GameObject gameObject, Viewport viewport) {
        updateViewportData(viewport);
    }

    /**
     * Отрисовка фона
     */
    @Override
    public void render(SpriteBatch batch) {
        // Получаем текстуру из TextureManager
        Texture texture = TextureManager.getInstance().getTexture(textureId);
        if (texture != null) {
            // Отрисовываем фон по всему экрану
            batch.draw(texture, x, y, width, height);
        }
    }

    /**
     * Освобождение ресурсов
     */
    @Override
    public void dispose() {
        // Ресурсы освобождаются в TextureManager
    }
}
