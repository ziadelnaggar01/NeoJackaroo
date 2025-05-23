package view.splashScene;

import controller.MusicManager;
import controller.SceneConfig;
import controller.SoundManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private MediaView mediaView;

	// Flags to track completion of each task
	private boolean preloadDone = false;
	private boolean videoDone = false;

	@FXML
	public void initialize() {
		// 1) Start the video
		Media media = new Media(getClass().getResource("/view/assets/hype_video.mp4").toExternalForm());
		MediaPlayer player = new MediaPlayer(media);
		mediaView.setMediaPlayer(player);

		// When video ends, set videoDone and attempt transition
		player.setOnEndOfMedia(() -> {
			videoDone = true;
			attemptSwitch(player);
		});

		player.play();

		// 2) Kick off FXML preload in background
		Task<Void> preload = new Task<>() {
			@Override
			protected Void call() {
				SceneConfig.getInstance(); // loads all scenes
				SoundManager.getInstance().preloadSound("button_click", "/view/assets/audio/button click.mp3");
				SoundManager.getInstance().preloadSound("Marble_Selection", "/view/assets/audio/Marble Selection.mp3");
				SoundManager.getInstance().preloadSound("Card_Selection", "/view/assets/audio/Card Selection.mp3");
				SoundManager.getInstance().preloadSound("errorSoundEffect", "/view/assests/sound/errorSoundEffect.mp3");
				SoundManager.getInstance().preloadSound("skipTurnSound", "/view/assests/sound/skipTurnSound.mp3");
				return null;
			}
		};

		// When preload finishes, set preloadDone and attempt transition
		preload.setOnSucceeded(evt -> {
			preloadDone = true;
			attemptSwitch(player);
		});

		Thread preloadThread = new Thread(preload, "Preload-FXML-Thread");
		preloadThread.setDaemon(true);
		preloadThread.start();
	}

	/**
	 * Only when both preload and video are done will this actually switch
	 */
	private void attemptSwitch(MediaPlayer player) {
		if (!preloadDone || !videoDone) {
			return; // Wait until both tasks complete
		}

		// Stop the video if it's still playing (defensive)
		if (player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.stop();
		}

		// Start background music
		MusicManager.getInstance().playMusic("/view/assets/audio/Digital Voyage - Twin Musicom.mp3");

		// Swap to the real start menu on the JavaFX thread
		Platform.runLater(() -> {
			try {
				Stage stage = (Stage) mediaView.getScene().getWindow();
				Parent start = SceneConfig.getInstance().getStartScene();
				stage.getScene().setRoot(start);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
