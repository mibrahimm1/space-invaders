package com.ibrahim;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private Clip backgroundMusic;
    private Clip fireSound;
    private Clip hitSound;
    private Clip levelUpSound;
    private Clip gameOverSound;

    public SoundManager() {
        try {
            backgroundMusic = loadClip("/sounds/background.wav");
            fireSound = loadClip("/sounds/fire.wav");
            hitSound = loadClip("/sounds/hit.wav");
            levelUpSound = loadClip("/sounds/level_up.wav");
            gameOverSound = loadClip("/sounds/game_over.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new IOException("Sound file not found: " + path);
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void playFireSound() {
        if (fireSound != null) {
            fireSound.setFramePosition(0);
            fireSound.start();
        }
    }

    public void playHitSound() {
        if (hitSound != null) {
            hitSound.setFramePosition(0);
            hitSound.start();
        }
    }

    public void playLevelUpSound() {
        if (levelUpSound != null) {
            levelUpSound.setFramePosition(0);
            levelUpSound.start();
        }
    }

    public void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }
}
