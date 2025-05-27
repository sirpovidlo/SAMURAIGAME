package com.mygame.model;

/**
 * Базовый интерфейс для всех игровых объектов
 */
public interface GameObject {
    /**
     * Получить уникальный идентификатор объекта
     */
    String getId();

    /**
     * Обновление состояния объекта
     * @param context контекст модели для безопасного доступа к данным
     */
    void update(ModelContext context);

    /**
     * Освобождение ресурсов
     */
    void dispose();
}
