package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
	private static SoundManager instance = new SoundManager();
	private Map<String, Media> soundLibrary; // Preloaded sound effects
	private double volume = 0.25; // Default volume

	private SoundManager() {
		soundLibrary = new HashMap<>();
	}

	public static SoundManager getInstance() {
		return instance;
	}

	// Preload a sound effect for repeated use
	public void preloadSound(String key, String filePath) {
		try {
			URL resource = getClass().getResource(filePath);
			if (resource != null) {
				soundLibrary.put(key, new Media(resource.toExternalForm()));
			}
		} catch (Exception e) {
			System.err.println("Failed to preload sound '" + key + "': " + e.getMessage());
		}
	}

	// Play a preloaded sound effect
	public void playSound(String key) {
		Media media = soundLibrary.get(key);
		if (media == null) {
			System.err.println("Sound '" + key + "' not found in library!");
			return;
		}

		try 
		{
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(volume);
			mediaPlayer.play();

			// Cleanup after playback finishes
			mediaPlayer.setOnEndOfMedia(() -> {
				mediaPlayer.dispose();
			});
		} 
		catch (Exception e) 
		{
			System.err.println("Error playing sound '" + key + "': " + e.getMessage());
		}
	}

	// Play a sound effect without preloading (for one-time use)
	public void playSoundOnce(String filePath) {
		try 
		{
			URL resource = getClass().getResource(filePath);
			if (resource != null) {
				Media media = new Media(resource.toExternalForm());
				MediaPlayer mediaPlayer = new MediaPlayer(media);
				mediaPlayer.setVolume(volume);
				mediaPlayer.play();
				mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
			}
		} 
		catch (Exception e) 
		{
			System.err.println("Error playing sound: " + e.getMessage());
		}
	}

	public void setVolume(double volume) 
	{
		this.volume = Math.max(0, Math.min(1, volume)); // Clamp between 0-1
	}
	
	public double getVolume() {
		return this.volume;
	}
}