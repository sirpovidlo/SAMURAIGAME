package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.GameObject;
import com.mygame.model.Player;
import com.mygame.model.Player.PlayerState;
import com.mygame.view.TextureManager;

/**
 * Модель представления для игрока
 */
public class PlayerViewModel implements ViewModel {
    private final String gameObjectId;

    // Размеры для отрисовки
    private float width;
    private float height;

    // Данные для отрисовки
    private float x;
    private float y;
    private boolean facingRight;
    private PlayerState state;

    public PlayerViewModel(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    @Override
    public String getGameObjectId() {
        return gameObjectId;
    }

    @Override
    public void update(GameObject gameObject, Viewport viewport) {
        if (gameObject instanceof Player) {
            Player player = (Player) gameObject;
            Vector2 position = player.getPosition();
            x = position.x;
            y = position.y;
            facingRight = player.isFacingRight();
            state = player.getCurrentState();
            
            // Получаем размеры из Player для синхронизации
            width = player.getWidth();
            height = player.getHeight();
        }
    }

    /**
     * Получение идентификатора текстуры в зависимости от состояния игрока
     */
    private String getTextureId() {
        switch (state) {
            case JUMPING:
                return TextureManager.PLAYER_JUMP;
            case CROUCHING:
                return TextureManager.PLAYER_CROUCH;
            case STANDING:
            default:
                return TextureManager.PLAYER;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Texture texture = TextureManager.getInstance().getTexture(getTextureId());
        if (texture != null) {
            if (facingRight) {
                batch.draw(texture,
                    x - width/2, y - height/2,
                    width, height);
            } else {
                batch.draw(texture,
                    x + width/2, y - height/2,
                    -width, height);
            }
        }
    }

    @Override
    public void dispose() {
        // Ресурсы освобождаются в TextureManager
    }
}
