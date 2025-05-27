package com.mygame.controller;

import com.mygame.model.GameObject;

/**
 * Интерфейс для контроллеров игровых объектов
 * @param <O> тип контролируемого игрового объекта
 */
public interface Controller<O extends GameObject> {
    /**
     * Получение типа контролируемого объекта
     * @return класс контролируемого объекта
     */
    Class<O> getObjectType();
    
    /**
     * Применение ввода к контролируемому объекту
     * @param controlledObject объект для применения ввода
     */
    void apply(O controlledObject);
}
