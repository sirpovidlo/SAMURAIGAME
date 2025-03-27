package com.mygame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

// Абстрактный класс, представляющий игровой объект
public abstract class GameObject {
    protected Body body; // Физическое тело объекта в Box2D
    protected Texture texture; // Текстура для отображения объекта

    // Конструктор инициализирует объект в мире Box2D
    public GameObject(World world, float x, float y) {
        // Должен быть реализован в подклассах
    }

    public Body getBody() {
        return body; // Получить физическое тело
    }

    public Texture getTexture() {
        return texture; // Получить текстуру объекта
    }

    // Абстрактный метод, который должен быть реализован в наследниках (обновление состояния)
    public abstract void update();

    // Метод освобождения ресурсов
    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Освобождаем текстуру из памяти
        }
    }
}
