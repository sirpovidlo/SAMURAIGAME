package com.mygame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Хранилище анимаций. Часть слоя View.
 * Управляет загрузкой и хранением анимаций персонажа.
 */
public class AnimationStorage implements Disposable {
    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private Animation<TextureRegion> squatAnimation;
    private Animation<TextureRegion> landingAnimation;
    private final Array<Texture> textures;

    public AnimationStorage() {
        textures = new Array<>();
        loadAnimations();
    }

    private void loadAnimations() {
        // Загрузка анимации стояния
        Texture standingTexture = new Texture(Gdx.files.internal("stand.png"));
        textures.add(standingTexture);
        Array<TextureRegion> standingFrames = new Array<>();
        standingFrames.add(new TextureRegion(standingTexture));
        standingAnimation = new Animation<>(0.1f, standingFrames);

        // Загрузка анимации прыжка
        Texture jumpingTexture = new Texture(Gdx.files.internal("jump.png"));
        textures.add(jumpingTexture);
        Array<TextureRegion> jumpingFrames = new Array<>();
        jumpingFrames.add(new TextureRegion(jumpingTexture));
        jumpingAnimation = new Animation<>(0.1f, jumpingFrames);

        // Загрузка анимации приседания
        Texture squatTexture = new Texture(Gdx.files.internal("squat.png"));
        textures.add(squatTexture);
        Array<TextureRegion> squatFrames = new Array<>();
        squatFrames.add(new TextureRegion(squatTexture));
        squatAnimation = new Animation<>(0.1f, squatFrames);

        // Анимация приземления использует те же кадры, что и приседание
        landingAnimation = squatAnimation;
    }

    public Animation<TextureRegion> getStandingAnimation() {
        return standingAnimation;
    }

    public Animation<TextureRegion> getJumpingAnimation() {
        return jumpingAnimation;
    }

    public Animation<TextureRegion> getSquatAnimation() {
        return squatAnimation;
    }

    public Animation<TextureRegion> getLandingAnimation() {
        return landingAnimation;
    }

    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }
}
