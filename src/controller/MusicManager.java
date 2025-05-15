package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    private static MusicManager instance = new MusicManager();

    private MediaPlayer mediaPlayer;
    
    private double volume = 0.5; // Default volume (50%)

    private MusicManager() {} // Private constructor for singleton

    public static MusicManager getInstance() {
        return instance;
    }

    public void playMusic(String filePath) {
        stop(); // Stop existing music if playing
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }
    
    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
}
