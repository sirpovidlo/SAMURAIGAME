package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameModel;
import com.mygame.view.TextureManager;

/**
 * Модель представления для земли (ViewModel в MVVM)
 * Преобразует данные модели в формат, удобный для отображения
 */
public class GroundViewModel implements ViewModel {
    private GameModel gameModel;
    private Viewport viewport;
    
    // Данные для отрисовки
    private float x;
    private float y;
    private float width;
    private float height = 2; // Высота земли
    
    // Ссылка на текстуру из TextureManager
    private String textureId = TextureManager.GROUND;

    /**
     * Создание модели представления для земли
     * @param gameModel модель игры с данными о позиции земли
     * @param viewport видовой экран для определения размеров
     */
    public GroundViewModel(GameModel gameModel, Viewport viewport) {
        this.gameModel = gameModel;
        this.viewport = viewport;
        
        // Вычисляем начальные данные для отрисовки
        update();
    }

    /**
     * Обновление данных представления
     */
    @Override
    public void update() {
        // Получаем позицию земли из модели
        Vector2 groundPos = gameModel.getGroundPosition();
        
        // Преобразуем данные модели в формат для отображения
        width = viewport.getWorldWidth();
        x = groundPos.x - width/2;
        y = groundPos.y - 1; // Смещаем вниз для выравнивания
    }

    /**
     * Отрисовка земли
     */
    @Override
    public void render(SpriteBatch batch) {
        // Получаем текстуру из TextureManager
        Texture texture = TextureManager.getInstance().getTexture(textureId);
        if (texture != null) {
            // Отрисовка земли с использованием подготовленных данных
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