package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

	private static MusicManager instance = new MusicManager();

	private MediaPlayer mediaPlayer;

	private double volume = 0.75; // Default volume (75%)

	private MusicManager() {} // Private constructor for singleton

	public static MusicManager getInstance() {
		return instance;
	}

	public void playMusic(String filePath) {
		try
		{
			stop(); // Stop existing music if playing
			Media media = new Media(getClass().getResource(filePath).toExternalForm());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
			mediaPlayer.setVolume(volume);
			mediaPlayer.play();
		}
		catch (NoClassDefFoundError e)
		{
			System.err.println("JavaFX Media module is missing! Music playback disabled.");
			// Optionally disable music features or fallback behavior here
		} 
		catch (Exception e) 
		{
			System.err.println("Failed to play music: " + e.getMessage());
		}
	}

	public void setVolume(double volume) {
		this.volume = volume;
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(volume);
		}
	}
	
	public double getVolume() {
		return this.volume;
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
