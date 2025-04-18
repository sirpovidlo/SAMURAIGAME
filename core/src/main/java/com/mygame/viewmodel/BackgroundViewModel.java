package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.view.TextureManager;

/**
 * Модель представления для фона
 * Преобразует данные модели в формат, удобный для отображения
 */
public class BackgroundViewModel implements ViewModel {
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

        // Вычисляем данные для отрисовки
        update();
    }

    /**
     * Обновление данных представления
     */
    @Override
    public void update() {
        // Получаем размеры вьюпорта для определения размеров фона
        width = viewport.getWorldWidth();
        height = viewport.getWorldHeight();
        x = 0;
        y = 0;
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
