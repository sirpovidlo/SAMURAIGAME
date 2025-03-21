package com.mygame.viewmodel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.model.GameObject;
import com.mygame.model.Player;

public class InputHandler {

    public void handleInput(GameObject gameObject) {
        if (gameObject instanceof Player) { // Проверяем, является ли объект игроком
            Player player = (Player) gameObject; // Приводим тип

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.moveRight(); // Вызов метода движения вправо
            }
        }
    }

    public void dispose() {
        // Освобождаем ресурсы, если нужно
    }
}
