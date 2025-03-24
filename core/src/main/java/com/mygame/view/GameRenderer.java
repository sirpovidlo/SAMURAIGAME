package com.mygame.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygame.model.GameWorld;
import com.badlogic.gdx.Gdx;

public class GameRenderer {
    private SpriteBatch batch;
    private Texture groundTexture;
    private Texture backgroundImage;
    private GameWorld gameWorld;
    private Box2DDebugRenderer debugRenderer; // Отладочный рендерер для хитбоксов
    private World world;

    public GameRenderer(SpriteBatch batch, GameWorld gameWorld) {
        this.batch = batch;
        this.gameWorld = gameWorld;
        this.world = gameWorld.getWorld(); // Получаем физический мир

        backgroundImage = new Texture("background.jpg"); // Фон игры
        groundTexture = new Texture("groud1.png"); // Текстура земли

        debugRenderer = new Box2DDebugRenderer(); // Создаем отладочный рендерер
    }

    public void render() {
        batch.begin();

        // Рисуем фон
        batch.draw(backgroundImage, 0, 0, 800, 600);

        // Рисуем землю
        // Рисуем землю
        Vector2 groundPos = gameWorld.getGroundPosition();
        float screenWidth = Gdx.graphics.getWidth();
        batch.draw(groundTexture,
            groundPos.x - screenWidth / 2, groundPos.y - 1,
            screenWidth, 2); // Ширина пола равна ширине экрана



        // Рисуем игрока
        Vector2 playerPos = gameWorld.getPlayer().getBody().getPosition();
        float width = 40f;  // Должно совпадать с хитбоксом (2*2)
        float height = 60f; // Должно совпадать с хитбоксом (3*2)
        batch.draw(gameWorld.getPlayer().getTexture(),
            playerPos.x - width / 2, playerPos.y - height / 2,
            width, height);


        batch.end();

        // Рисуем хитбоксы
        debugRenderer.render(world, batch.getProjectionMatrix());
    }


    public void dispose() {
        groundTexture.dispose();
        backgroundImage.dispose();
        debugRenderer.dispose(); // Освобождаем ресурсы отладочного рендерера
    }
}
