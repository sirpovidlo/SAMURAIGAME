package com.mygame.viewmodel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.model.GameObject;
import com.mygame.model.Player;

public class InputHandler {
    public void handleInput(GameObject gameObject) {
        if (gameObject instanceof Player) {
            Player player = (Player) gameObject;

            boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D);
            boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A);

            // Если клавиша D нажата, двигаемся вправо
            if (movingRight) {
                player.moveRight();
            }
            // Если клавиша A нажата, двигаемся влево
            else if (movingLeft) {
                player.moveLeft();
            }
            // Если клавиши не нажаты, останавливаем движение
            else {
                player.stopMovement();  // Метод, который останавливает персонажа
            }
        }
    }




    public void dispose() {
        // Освобождаем ресурсы, если нужно
    }
}
