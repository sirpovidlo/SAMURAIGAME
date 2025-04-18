package com.mygame.model;

/**
 * Интерфейс, представляющий игровой объект
 */
public interface GameObject {
    /**
     * Метод для обновления состояния объекта
     */
    void update();
    
    /**
     * Метод освобождения ресурсов
     */
    void dispose();
}
