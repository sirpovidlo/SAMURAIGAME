package com.mygame.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.GameWorld;

// Класс для рендеринга игры
public class GameRenderer {
    private SpriteBatch batch; // Спрайт-менеджер для отрисовки
    private Texture groundTexture; // Текстура пола
    private Texture backgroundImage; // Текстура фона
    private GameWorld gameWorld; // Игровой мир

    // Конструктор для инициализации рендерера
    public GameRenderer(SpriteBatch batch, GameWorld gameWorld) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        backgroundImage = new Texture("background1.jpg"); // Загружаем текстуру фона
        groundTexture = new Texture("groud1.png"); // Загружаем текстуру пола
    }

    // Метод для отрисовки всех объектов
    public void render() {
        batch.begin(); // Начало отрисовки

        // Отрисовка фона
        batch.draw(backgroundImage, 0, 0, 800, 600); // Отображаем фон

        // Получаем позицию пола из GameWorld
        float groundX = gameWorld.getGroundPosition().x; // Позиция по X (центр)
        float groundY = gameWorld.getGroundPosition().y;  // Позиция по Y (нижняя часть пола)
        batch.draw(groundTexture, groundX, groundY, 700, 35); // Отображаем пол

        // Отрисовка персонажа
        batch.draw(gameWorld.getPlayer().getTexture(),
            gameWorld.getPlayer().getBody().getPosition().x - 20, // Центрируем по X
            gameWorld.getPlayer().getBody().getPosition().y - 20, 80, 80); // Центрируем по Y


        batch.end(); // Завершаем отрисовку
    }

    // Освобождение ресурсов
    public void dispose() {
        groundTexture.dispose(); // Освобождаем текстуру пола
        backgroundImage.dispose(); // Освобождаем текстуру фона

    }
}
