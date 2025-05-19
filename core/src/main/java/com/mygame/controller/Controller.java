package com.mygame.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface Controller<T> {
    void update(float delta);
    void handleInput(InputEvent event);
    void setTarget(T target);
}
