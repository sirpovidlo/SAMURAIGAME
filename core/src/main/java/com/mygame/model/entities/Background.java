package com.mygame.model.entities;

import com.mygame.model.world.GameWorld;

/**
 * Сущность фона и земли. Часть слоя Model.
 */
public class Background implements Entity {
    private final int id;
    private final float width;
    private final float height;
    private final float groundHeight;

    public Background(int id, float width, float height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.groundHeight = -2f; 
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void update(GameWorld world) {
        // Фон статичен, обновление не требуется
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getGroundHeight() { return groundHeight; }
}
