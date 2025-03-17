package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.physics.box2d.*;

public class Main implements ApplicationListener {
    private Texture background;
    private Texture Earth;
    private SpriteBatch spriteBatch;
    private StretchViewport viewport;
    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> crouchingAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> squatingAnimation;
    private float stateTime;
    private float heroX = 0; // Начальная позиция X
    private float heroY = 0; // Начальная позиция Y
    private boolean facingRight = true; // По умолчанию персонаж смотрит вправо
    private Sound sound;
    private Sound soundcrouch;

    private World world; // Физический мир
    private Body groundBody; // Тело земли
    private Body heroBody; // Тело персонажа

    private enum HeroState {
        STANDING, CROUCHING, JUMPING, LANDING, SQUAT
    }

    private HeroState heroState = HeroState.STANDING; // Начальное состояние
    private float landingTimer = 0f; // Таймер для отслеживания времени после приземления
    private float jumpVelocity = 4f; // Начальная скорость прыжка
    private float gravity = -25f; // Гравитация (ускорение падения)

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        background = new Texture("forest.jpg");
        Earth = new Texture("earth.png");
        viewport = new StretchViewport(10, 5); // Соотношение 16:9
        sound = Gdx.audio.newSound(Gdx.files.internal("koks.mp3"));
        soundcrouch = Gdx.audio.newSound(Gdx.files.internal("soundcrouch.mp3"));

        // Создание физического мира
        world = new World(new Vector2(0, -9.8f), true); // Сила гравитации по умолчанию

        // Создание земли как статического тела
        createGround();

        // Создание персонажа
        createHero();


        // Загрузка кадров для состояния стояния
        Array<TextureRegion> standingFrames = new Array<>();
        standingFrames.add(new TextureRegion(new Texture("stand.png"))); // Один кадр для стояния
        standingAnimation = new Animation<>(0.1f, standingFrames);

        // Загрузка кадров для состояния приседания
        Array<TextureRegion> crouchingFrames = new Array<>();
        crouchingFrames.add(new TextureRegion(new Texture("crouch.png"))); // Один кадр для приседания
        crouchingAnimation = new Animation<>(0.1f, crouchingFrames);

        // Загрузка кадров для состояния прыжка
        Array<TextureRegion> jumpingFrames = new Array<>();
        jumpingFrames.add(new TextureRegion(new Texture("jump.png"))); // Один кадр для прыжка
        jumpingAnimation = new Animation<>(0.1f, jumpingFrames);

        Array<TextureRegion> squatingFrames = new Array<>();
        squatingFrames.add(new TextureRegion(new Texture("squat.png"))); // Один кадр для приседа
        squatingAnimation = new Animation<>(0.1f, squatingFrames);

        // Начальная анимация — стояние
        currentAnimation = standingAnimation;
        stateTime = 0f; // Инициализация времени
    }

    private void createHero() {
        // Создаем тело персонажа
        BodyDef heroBodyDef = new BodyDef();
        heroBodyDef.type = BodyDef.BodyType.DynamicBody;
        heroBodyDef.position.set(heroX, 2); // Начальная позиция персонажа

        heroBody = world.createBody(heroBodyDef);

        // Создаем форму персонажа (в данном случае прямоугольник)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f); // Размеры персонажа

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // Устанавливаем плотность тела
        heroBody.createFixture(fixtureDef);
        shape.dispose(); // Освобождаем ресурсы
    }

    private void createGround() {
        // Создаем землю как статическое тело
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody; // Статическое тело
        groundBodyDef.position.set(0, -1); // Позиция земли (по оси Y)

        groundBody = world.createBody(groundBodyDef);

        // Формируем землю как прямоугольник
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(10f, 1f); // Ширина земли = 10, высота = 1

        // Создаем Fixture для тела земли
        groundBody.createFixture(groundShape, 0); // Масса 0, потому что это статическое тело

        groundShape.dispose(); // Освобождаем память после использования
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        input();
        logic();
        world.step(1 / 60f, 6, 2); // Обновляем физический мир

        draw();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        // Управление движением (D - вправо, A - влево)
        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            heroX += speed * delta;
            if (!facingRight) {
                facingRight = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            heroX -= speed * delta;
            if (facingRight) {
                facingRight = false;
            }
        }

        // Прыжок при нажатии W
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && heroState == HeroState.STANDING) {
            heroState = HeroState.JUMPING; // Переводим в состояние прыжка
            currentAnimation = jumpingAnimation; // Анимация прыжка
            jumpVelocity = 12f; // Начальная скорость прыжка
        }

        // Приседание при нажатии S
        if (Gdx.input.isKeyPressed(Input.Keys.S) && heroState == HeroState.STANDING) {
            heroState = HeroState.SQUAT;
            currentAnimation = squatingAnimation;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.S) && heroState == HeroState.SQUAT) {
            heroState = HeroState.STANDING;
            currentAnimation = standingAnimation;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            sound.play();
        }
        
    }


    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        // Обрабатываем состояние прыжка
        if (heroState == HeroState.JUMPING) {
            heroY += jumpVelocity * delta;
            jumpVelocity += gravity * delta; // Применяем гравитацию

            // Проверяем, приземлился ли персонаж
            if (heroY <= 0) {
                heroY = 0;
                heroState = HeroState.LANDING; // Переходим в состояние приземления
                soundcrouch.play(); // Звук приземления
                currentAnimation = crouchingAnimation; // Переходим в анимацию приземления
                landingTimer = 0.3f; // Задержка на приземление (0.3 секунды)
            }
        }

        // После приземления автоматически переходим в стояние
        if (heroState == HeroState.LANDING) {
            landingTimer -= delta;
            if (landingTimer <= 0) {
                heroState = HeroState.STANDING; // После задержки переходим в стояние
                currentAnimation = standingAnimation; // Переходим в анимацию стояния
            }
        }

        // Ограничиваем героя в пределах экрана
        float worldWidth = viewport.getWorldWidth();
        heroX = MathUtils.clamp(heroX, 0, worldWidth - 2);
    }


    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // Отрисовка фона
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(Earth, 0, -2, viewport.getWorldWidth(), 1); // Земля (платформа)

        // Отрисовка текущего кадра анимации
        stateTime += Gdx.graphics.getDeltaTime(); // Обновляем время анимации
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // Отражаем текстуру, если нужно
        if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        // Отрисовка персонажа
        // Используем физическое положение героя для отрисовки
        spriteBatch.draw(currentFrame, heroX, heroY + 0.2f, 1, 1); // heroX и heroY — координаты героя

        spriteBatch.end();
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();
        Earth.dispose();
        standingAnimation.getKeyFrame(0).getTexture().dispose();
        crouchingAnimation.getKeyFrame(0).getTexture().dispose();
        jumpingAnimation.getKeyFrame(0).getTexture().dispose();
    }
}
