package com.mygame.viewmodel;

import com.mygame.model.GameWorld;

public class GameLogic {
    private GameWorld gameWorld;

    public GameLogic(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void update(float deltaTime) {
        // Обновление мира, физики и т. д.
        gameWorld.update(deltaTime);
    }

    public void dispose() {
        // Освобождение ресурсов
        gameWorld.dispose();
    }
}
