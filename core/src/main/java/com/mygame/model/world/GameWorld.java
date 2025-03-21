package com.mygame.model.world;

import com.badlogic.gdx.Gdx;
import com.mygame.model.entities.Entity;
import java.util.*;

/**
 * Игровой мир, содержащий всю игровую логику.
 * Часть слоя Model, не имеет зависимостей от View или ViewModel
 */
public class GameWorld {
    private final float width;
    private final float height;
    private final float gravity = -25f;
    private final Map<Integer, Entity> entities;
    
    public GameWorld(float width, float height) {
        this.width = width;
        this.height = height;
        this.entities = new HashMap<>();
    }
    
    public void update() {
        for (Entity entity : entities.values()) {
            entity.update(this);
        }
    }
    
    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }
    
    public Entity getEntity(int id) {
        return entities.get(id);
    }
    
    public Collection<Entity> getEntities() {
        return Collections.unmodifiableCollection(entities.values());
    }
    
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getGravity() { return gravity; }
    public float getDeltaTime() { return Gdx.graphics.getDeltaTime(); }
}
