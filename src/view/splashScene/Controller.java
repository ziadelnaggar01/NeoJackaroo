package view.splashScene;

import controller.MusicManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import controller.SceneConfig;
import controller.SoundManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.media.Media;

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
		Task<Void> preload = new Task<Void>() {
			@Override
			protected Void call() {
				SceneConfig.getInstance(); // loads all scenes
				SoundManager.getInstance().preloadSound("button_click", "/view/assets/audio/button click.mp3");
				SoundManager.getInstance().preloadSound("Marble_Selection", "/view/assets/audio/Marble Selection.mp3");
				SoundManager.getInstance().preloadSound("Card_Selection", "/view/assets/audio/Card Selection.mp3");
				SoundManager.getInstance().preloadSound("errorSoundEffect", "/view/assests/sound/errorSoundEffect.mp3");
				SoundManager.getInstance().preloadSound("skipTurnSound", "/view/assests/sound/skipTurnSound.mp3");
				SoundManager.getInstance().preloadSound("playCardSoundEffect",
						"/view/assests/sound/playCardSoundEffect.mp3");
				SoundManager.getInstance().preloadSound("trapSoundEffect", "/view/assests/sound/trapSoundEffect.mp3");
				SoundManager.getInstance().preloadSound("playCardSoundEffect", "/view/assests/sound/playCardSoundEffect.mp3");
				SoundManager.getInstance().preloadSound("trapSoundEffect", "/view/assests/sound/trapSoundEffect.mp3");

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
		if (!preloadDone || !videoDone)
			return;

		Platform.runLater(() -> {
			try {
				Stage stage = (Stage) mediaView.getScene().getWindow();

				// 1. Flash to white overlay
				Parent videoRoot = mediaView.getScene().getRoot();
				Region whiteFlash = new Region();
				whiteFlash.setStyle("-fx-background-color: white;");
				whiteFlash.setOpacity(0);
				whiteFlash.setPrefSize(videoRoot.getBoundsInParent().getWidth(),
						videoRoot.getBoundsInParent().getHeight());

				((Pane) videoRoot).getChildren().add(whiteFlash);

				FadeTransition flash = new FadeTransition(Duration.millis(150), whiteFlash);
				flash.setFromValue(0);
				flash.setToValue(1);
				flash.setOnFinished(e -> {
					// 2. Stop video and audio after flash
					player.stop();

					// 3. Start background music with fade-in
					MusicManager.getInstance().playMusic("/view/assets/audio/Digital Voyage - Twin Musicom.mp3");
					MusicManager.getInstance().getMediaPlayer().setVolume(0); // start
																				// silent

					Timeline volumeUp = new Timeline(new KeyFrame(Duration.seconds(0.5),
							new KeyValue(MusicManager.getInstance().getMediaPlayer().volumeProperty(), 0.75)));
					volumeUp.play();

					// 4. Load and fade in start scene
					Parent start = SceneConfig.getInstance().getStartScene();
					start.setOpacity(0);
					stage.getScene().setRoot(start);

					FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), start);
					fadeIn.setFromValue(0);
					fadeIn.setToValue(1);
					fadeIn.play();
				});

				flash.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

}
