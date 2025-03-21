package com.mygame.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Базовый интерфейс для всех отображаемых объектов.
 * Часть слоя View, не знает о Model
 */
public interface Drawable {
    void update(float deltaTime);
    void draw(SpriteBatch batch);
    int getZIndex(); // Для определения порядка отрисовки
}
