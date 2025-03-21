package com.mygame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

// Абстрактный класс для всех объектов игры
public abstract class GameObject {
    protected Body body; // Физическое тело объекта
    protected Texture texture; // Текстура объекта

    // Конструктор для создания объекта
    public GameObject(World world, float x, float y) {
        // Базовый конструктор
    }

    public Body getBody() {
        return body; // Возвращаем физическое тело
    }

    public Texture getTexture() {
        return texture; // Возвращаем текстуру
    }

    // Абстрактный метод для обновления состояния объекта
    public abstract void update();

    // Освобождение ресурсов, связанных с объектом
    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Освобождаем текстуру
        }
    }
}
