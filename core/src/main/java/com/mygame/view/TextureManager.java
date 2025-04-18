package com.mygame.view;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер текстур игры (часть View в MVVM)
 * Отвечает за загрузку, хранение и предоставление текстур
 */
public class TextureManager {
    // Константы для идентификации текстур
    public static final String PLAYER = "player";
    public static final String PLAYER_CROUCH = "player_crouch";
    public static final String PLAYER_JUMP = "player_jump";
    public static final String BACKGROUND = "background";
    public static final String GROUND = "ground";
    
    // Хранилище текстур
    private Map<String, Texture> textures;
    
    // Singleton экземпляр
    private static TextureManager instance;
    
    /**
     * Получение экземпляра TextureManager (Singleton)
     */
    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }
    
    /**
     * Приватный конструктор для реализации Singleton
     */
    private TextureManager() {
        textures = new HashMap<>();
        loadTextures();
    }
    
    /**
     * Загрузка всех текстур игры
     */
    private void loadTextures() {
        // Загрузка текстур персонажа
        textures.put(PLAYER, new Texture("player.png"));
        textures.put(PLAYER_CROUCH, new Texture("player_crouch.png"));
        textures.put(PLAYER_JUMP, new Texture("player_jump.png"));
        
        // Загрузка текстур окружения
        textures.put(BACKGROUND, new Texture("background.jpg"));
        textures.put(GROUND, new Texture("groud1.png"));
    }
    
    /**
     * Получение текстуры по идентификатору
     * 
     * @param textureId идентификатор текстуры
     * @return текстура или null, если не найдена
     */
    public Texture getTexture(String textureId) {
        return textures.get(textureId);
    }
    
    /**
     * Освобождение всех текстур
     */
    public void dispose() {
        for (Texture texture : textures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        textures.clear();
    }
} 