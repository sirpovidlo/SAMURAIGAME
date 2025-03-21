package com.mygame.viewmodel;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygame.model.entities.Hero;
import com.mygame.model.entities.Hero.HeroState;
import com.mygame.view.AnimationStorage;
import com.mygame.view.Drawable;

/**
 * ViewModel для героя. Связывает модель героя с его отображением.
 * Знает как о Model (Hero), так и о View (анимации)
 */
public class HeroViewModel implements Drawable {
    private final Hero hero;
    private final AnimationStorage animations;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime;
    private static final float SPRITE_WIDTH = 1f;   
    private static final float SPRITE_HEIGHT = 1f;  

    public HeroViewModel(Hero hero, AnimationStorage animations) {
        this.hero = hero;
        this.animations = animations;
        this.currentAnimation = animations.getStandingAnimation();
        this.stateTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;

        // Обновляем текущую анимацию в зависимости от состояния героя
        switch (hero.getState()) {
            case STANDING:
                currentAnimation = animations.getStandingAnimation();
                break;
            case JUMPING:
                currentAnimation = animations.getJumpingAnimation();
                break;
            case LANDING:
            case SQUAT:
                currentAnimation = animations.getSquatAnimation();
                break;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        float width = SPRITE_WIDTH; 
        float height = SPRITE_HEIGHT;

        // Отражаем спрайт если персонаж смотрит влево
        if (!hero.isFacingRight()) {
            width = -width;
        }

        batch.draw(currentFrame, 
                  hero.getX() - width/2, hero.getY(),  // позиция
                  width, height);                       // размеры
    }

    @Override
    public int getZIndex() {
        return 1; // Герой отрисовывается поверх фона
    }
}
