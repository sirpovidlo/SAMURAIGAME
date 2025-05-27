package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameObject;

/**
 * Интерфейс для модели представления в паттерне MVVM
 *
 * ViewModel отвечает за:
 * 1. Преобразование данных из Model в формат, удобный для View
 * 2. Хранение подготовленных данных для отрисовки
 * 3. Предоставление методов для отрисовки данных
 */
public interface ViewModel {
    /**
     * Получить идентификатор связанного игрового объекта
     */
    String getGameObjectId();

    /**
     * Обновление данных представления на основе данных модели
     * Вызывается перед отрисовкой для обновления внутреннего состояния
     */
    void update(GameObject gameObject, Viewport viewport);

    /**
     * Отрисовка объекта с использованием подготовленных данных
     *
     * @param batch SpriteBatch для отрисовки
     */
    void render(SpriteBatch batch);

    /**
     * Освобождение ресурсов, связанных с этой моделью представления
     */
    void dispose();
}
