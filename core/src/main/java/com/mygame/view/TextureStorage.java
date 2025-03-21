package com.mygame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище текстур. Часть слоя View.
 * Управляет загрузкой и хранением текстур.
 */
public class TextureStorage implements Disposable {
    private final Map<String, Texture> textures;

    public TextureStorage() {
        this.textures = new HashMap<>();
        loadTextures();
    }

    private void loadTextures() {
        // Загружаем все необходимые текстуры
        loadTexture("forest", "forest.jpg");
        loadTexture("earth", "earth.png");
    }

    private void loadTexture(String name, String path) {
        textures.put(name, new Texture(Gdx.files.internal(path)));
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
