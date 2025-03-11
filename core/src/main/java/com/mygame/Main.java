package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    private Texture background;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float heroX = 0; // Начальная позиция X
    private float heroY = 0; // Начальная позиция Y

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        background = new Texture("background.png");
        viewport = new FitViewport(8, 5); // Соотношение 16:9

        // Загрузка кадров анимации
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= 10; i++) { // Предположим, у вас 10 кадров
            Texture frame = new Texture(Gdx.files.internal("frame" + i + ".png"));
            frames.add(new TextureRegion(frame));
        }

        // Создание анимации (0.1f — время между кадрами в секундах)
        animation = new Animation<>(0.1f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP); // Зацикленная анимация
        stateTime = 0f; // Инициализация времени
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            heroX += speed * delta; // Движение вправо
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            heroX -= speed * delta; // Движение влево
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Ограничиваем позицию героя в пределах экрана
        heroX = MathUtils.clamp(heroX, 0, worldWidth - 2); // 2 — ширина героя
        heroY = MathUtils.clamp(heroY, 0, worldHeight - 2); // 2 — высота героя
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // Отрисовка фона на весь экран
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Отрисовка текущего кадра анимации
        stateTime += Gdx.graphics.getDeltaTime(); // Обновляем время анимации
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // Получаем текущий кадр

        // Отрисовка героя (теперь это текущий кадр анимации)
        spriteBatch.draw(currentFrame, heroX, heroY, 2, 2); // heroX и heroY — координаты героя

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();
        for (TextureRegion frame : animation.getKeyFrames()) {
            frame.getTexture().dispose(); // Освобождение ресурсов
        }
    }
}
