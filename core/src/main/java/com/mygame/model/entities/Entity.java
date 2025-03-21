package com.mygame.model.entities;

import com.mygame.model.world.GameWorld;

/**
 * Базовый интерфейс для всех игровых сущностей.
 * Часть слоя Model, не имеет зависимостей от View или ViewModel
 */
public interface Entity {
    int getId();
    void update(GameWorld world);
}
