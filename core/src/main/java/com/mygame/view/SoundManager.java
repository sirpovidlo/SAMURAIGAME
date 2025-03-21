package com.mygame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Менеджер звуков игры. Часть слоя View.
 * Управляет загрузкой и воспроизведением звуков и музыки.
 */
public class SoundManager implements Disposable {
    private Music backgroundMusic;
    private Sound crouchSound;
    private boolean isMusicPlaying;

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("koks.mp3"));
        backgroundMusic.setLooping(true);
        crouchSound = Gdx.audio.newSound(Gdx.files.internal("soundcrouch.mp3"));
    }

    public void playBackgroundMusic() {
        if (!isMusicPlaying) {
            backgroundMusic.play();
            isMusicPlaying = true;
        }
    }

    public void stopBackgroundMusic() {
        if (isMusicPlaying) {
            backgroundMusic.stop();
            isMusicPlaying = false;
        }
    }

    public void playCrouchSound() {
        crouchSound.play();
    }

    @Override
    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        if (crouchSound != null) {
            crouchSound.dispose();
        }
    }
}
