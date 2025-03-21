package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.entities.Background;
import com.mygame.view.Drawable;
import com.mygame.view.TextureStorage;

/**
 * ViewModel для фона и земли. Связывает Background (Model) с его отображением (View)
 */
public class BackgroundViewModel implements Drawable {
    private final Background background;
    private final TextureStorage textureStorage;

    public BackgroundViewModel(Background background, TextureStorage textureStorage) {
        this.background = background;
        this.textureStorage = textureStorage;
    }

    @Override
    public void update(float deltaTime) {
        // Фон статичен, обновление не требуется
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Отрисовка фона
        batch.draw(textureStorage.getTexture("forest"),
                  0, 0,
                  background.getWidth(),
                  background.getHeight());

        // Отрисовка земли
        batch.draw(textureStorage.getTexture("earth"),
                  0, background.getGroundHeight(),
                  background.getWidth(),
                  1);
    }

    @Override
    public int getZIndex() {
        return 0; // Фон отрисовывается первым
    }
}
