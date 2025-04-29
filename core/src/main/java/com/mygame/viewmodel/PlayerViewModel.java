package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameWorld;
import com.mygame.model.Player;
import com.mygame.view.TextureManager;

/**
 * Модель представления для игрока (ViewModel в MVVM)
 * Преобразует данные модели в формат, удобный для отображения
 */
public class PlayerViewModel implements ViewModel {
    // Используем глобальную константу масштабирования из GameWorld
    private Player player;

    // Размеры для отрисовки
    private float width = 3.75f;
    private float height = 5.645f;

    // Данные для отрисовки
    private float x;
    private float y;
    private boolean facingRight;


    // Константа интерполяции (0-1)
    // 1 = точное следование физике, 0 = плавное движение
    private static final float INTERPOLATION = 1.0f; // Убираем интерполяцию для точного соответствия хитбоксу

    // Текущее состояние игрока для выбора текстуры
    private Player.PlayerState currentState;

    /**
     * Создание модели представления для игрока
     * @param player модель игрока
     */
    public PlayerViewModel(Player player) {
        this.player = player;
    }

    @Override
    public void update(Viewport viewport) {
        // Получаем данные из модели
        Vector2 physicsPos = player.getBody().getPosition();
        facingRight = player.isFacingRight();
        currentState = player.getCurrentState();



        transformCoordinates(physicsPos, viewport);
    }
    private void transformCoordinates(Vector2 pos, Viewport viewport)
    {
        //Vector3 transformedCoordinate = viewport.getCamera().project(new Vector3(pos.x, pos.y, 0));
        //x = transformedCoordinate.x;
        //y = transformedCoordinate.y;
        x = pos.x;
        y = pos.y;
    }

    /**
     * Получение идентификатора текстуры в зависимости от состояния игрока
     */
    private String getTextureId() {
        switch (currentState) {
            case JUMPING:
                return TextureManager.PLAYER_JUMP;
            case CROUCHING:
                return TextureManager.PLAYER_CROUCH;
            case STANDING:
            default:
                return TextureManager.PLAYER;
        }
    }

    /**
     * Отрисовка игрока
     */
    @Override
    public void render(SpriteBatch batch) {
        // Получаем нужную текстуру из TextureManager в зависимости от состояния
        Texture texture = TextureManager.getInstance().getTexture(getTextureId());
        if (texture != null) {
            // Добавляем небольшое смещение вверх, чтобы персонаж не проваливался в землю
            float renderY = y - height/2 + 10;

            // Отрисовка игрока с учетом направления движения
            if (facingRight) {
                // Игрок смотрит вправо
                batch.draw(texture,
                    x - width/2, renderY,  // Позиция
                    width, height);        // Размеры
            } else {
                // Игрок смотрит влево (отзеркаливаем текстуру)
                batch.draw(texture,
                    x + width/2, renderY,  // Позиция со смещением для зеркалирования
                    -width, height);       // Отрицательная ширина для зеркалирования
            }
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
