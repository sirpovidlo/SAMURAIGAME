package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygame.model.world.GameWorld;
import com.mygame.model.entities.Hero;
import com.mygame.model.entities.Background;
import com.mygame.view.AnimationStorage;
import com.mygame.view.TextureStorage;
import com.mygame.view.SoundManager;
import com.mygame.view.Drawable;
import com.mygame.viewmodel.HeroViewModel;
import com.mygame.viewmodel.BackgroundViewModel;
import com.mygame.controller.GameController;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Основной класс приложения, который инициализирует и управляет игровым процессом.
 * Выступает в роли композиционного корня приложения, соединяя все слои вместе.
 */
public class Main implements ApplicationListener {
    // Model
    private GameWorld gameWorld;
    private Hero hero;
    private Background background;

    // View
    private SpriteBatch spriteBatch;
    private StretchViewport viewport;
    private AnimationStorage animationStorage;
    private TextureStorage textureStorage;
    private SoundManager soundManager;

    // ViewModel
    private HeroViewModel heroViewModel;
    private BackgroundViewModel backgroundViewModel;
    private List<Drawable> drawableEntities;

    // Controller
    private GameController gameController;

    @Override
    public void create() {
        // Инициализация View компонентов
        spriteBatch = new SpriteBatch();
        viewport = new StretchViewport(10, 5);
        textureStorage = new TextureStorage();
        animationStorage = new AnimationStorage();
        soundManager = new SoundManager();
        drawableEntities = new ArrayList<>();

        // Инициализация Model
        gameWorld = new GameWorld(10, 5);

        // Создание фона
        background = new Background(1, 10, 5);
        gameWorld.addEntity(background);

        // Создание героя
        hero = new Hero(2, 0.3f, 0.25f);
        gameWorld.addEntity(hero);

        // Инициализация ViewModel
        backgroundViewModel = new BackgroundViewModel(background, textureStorage);
        heroViewModel = new HeroViewModel(hero, animationStorage);

        // Добавляем все drawable сущности в список для отрисовки
        drawableEntities.add(backgroundViewModel);
        drawableEntities.add(heroViewModel);

        // Сортируем по Z-индексу для правильного порядка отрисовки
        drawableEntities.sort(Comparator.comparingInt(Drawable::getZIndex));

        // Инициализация Controller
        gameController = new GameController(gameWorld, hero);

        // Обработка музыки
        handleMusicInput();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обработка ввода (Controller)
        gameController.handleInput();

        // Обработка звуков
        handleSoundEffects();

        // Обработка музыки
        handleMusicInput();

        // Обновление состояния игры (Model)
        gameWorld.update();

        // Обновление ViewModel
        for (Drawable drawable : drawableEntities) {
            drawable.update(deltaTime);
        }

        // Отрисовка (View)
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        for (Drawable drawable : drawableEntities) {
            drawable.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private void handleSoundEffects() {
        // Проигрываем звук приземления только один раз
        if (hero.shouldPlayLandingSound()) {
            soundManager.playCrouchSound();
        }
    }

    private void handleMusicInput() {
        // Включаем/выключаем музыку по клавише M
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
                soundManager.playBackgroundMusic();
                Gdx.input.setInputProcessor(null);
            } else {
                soundManager.stopBackgroundMusic();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        animationStorage.dispose();
        textureStorage.dispose();
        soundManager.dispose();
    }
}
