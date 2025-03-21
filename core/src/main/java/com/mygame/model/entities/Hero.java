package com.mygame.model.entities;

import com.mygame.model.world.GameWorld;

/**
 * Сущность героя-самурая. Содержит только логику и состояние,
 * ничего не знает про отображение
 */
public class Hero implements Entity {
    private final int id;
    private float x;
    private float y;
    private float jumpVelocity;
    private boolean facingRight;
    private HeroState state;
    private boolean wasInAir;
    private final float GROUND_LEVEL = 0.25f; 
    private float landingTimer; // Таймер для задержки после приземления
    private static final float LANDING_DELAY = 0.3f; // Задержка в секундах
    private boolean landingSoundPlayed; // Флаг для отслеживания воспроизведения звука
    
    public enum HeroState {
        STANDING, JUMPING, LANDING, SQUAT
    }

    public Hero(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.jumpVelocity = 0;
        this.facingRight = true;
        this.state = HeroState.STANDING;
        this.wasInAir = false;
        this.landingTimer = 0;
        this.landingSoundPlayed = false;
    }

    @Override
    public void update(GameWorld world) {
        float delta = world.getDeltaTime();

        // Применяем гравитацию если в прыжке
        if (state == HeroState.JUMPING) {
            wasInAir = true;
            y += jumpVelocity * delta;
            jumpVelocity += world.getGravity() * delta;

            // Проверка приземления
            if (y <= GROUND_LEVEL) {  
                y = GROUND_LEVEL;     // Устанавливаем точную позицию земли
                if (wasInAir) {
                    state = HeroState.LANDING;
                    landingTimer = 0; // Сбрасываем таймер
                    landingSoundPlayed = false; // Сбрасываем флаг звука
                    wasInAir = false;
                }
            }
        } else if (state == HeroState.LANDING) {
            // Увеличиваем таймер
            landingTimer += delta;
            
            // Переходим в стойку только после задержки
            if (landingTimer >= LANDING_DELAY) {
                state = HeroState.STANDING;
                landingSoundPlayed = false; // Сбрасываем флаг при переходе в стойку
            }
        }
    }

    public void moveLeft(float delta, float speed) {
        if (state != HeroState.SQUAT) {
            x -= speed * delta;
            facingRight = false;
        }
    }

    public void moveRight(float delta, float speed) {
        if (state != HeroState.SQUAT) {
            x += speed * delta;
            facingRight = true;
        }
    }

    public void jump() {
        if (state == HeroState.STANDING) {
            state = HeroState.JUMPING;
            jumpVelocity = 12f;
        }
    }

    public void squat() {
        if (state == HeroState.STANDING) {
            state = HeroState.SQUAT;
        }
    }

    public void standUp() {
        if (state == HeroState.SQUAT) {
            state = HeroState.STANDING;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isFacingRight() { return facingRight; }
    public HeroState getState() { return state; }
    
    // Метод для проверки и установки флага звука приземления
    public boolean shouldPlayLandingSound() {
        if (!landingSoundPlayed && state == HeroState.LANDING) {
            landingSoundPlayed = true;
            return true;
        }
        return false;
    }
}
