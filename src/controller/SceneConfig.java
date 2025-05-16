package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class SceneConfig {
    // Singleton instance
    private static final SceneConfig INSTANCE = new SceneConfig();

    // Pre-loaded scenes
    private Parent startScene;
    private Parent endScene;
    private Parent howToPlayScene;
    private Parent settingsScene;
    private Parent playerNameScene;
    private Parent gameScene;
    private Parent exceptionScene;
    private boolean inGame = false;

    // Private constructor: load all FXML here
    private SceneConfig() {
        try {
            startScene      = FXMLLoader.load(getClass().getResource("/view/startMenu/Scene.fxml"));
            endScene        = FXMLLoader.load(getClass().getResource("/view/endScreen/Scene.fxml"));
            howToPlayScene  = FXMLLoader.load(getClass().getResource("/view/HowToPlayScreen/Scene.fxml"));
            playerNameScene = FXMLLoader.load(getClass().getResource("/view/PlayerName/Scene.fxml"));
            gameScene       = FXMLLoader.load(getClass().getResource("/view/BoardScene.fxml"));
            settingsScene   = FXMLLoader.load(getClass().getResource("/view/settingsMenu/SettingsScene.fxml"));
            exceptionScene  = FXMLLoader.load(getClass().getResource("/view/exception/ExceptionScene .fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            // You may want to throw a runtime exception here if any scene fails to load
        }
    }

    /** Global accessor */
    public static SceneConfig getInstance() {
        return INSTANCE;
    }

    /** Getters for each scene */
    public Parent getStartScene()      { return startScene; }
    public Parent getEndScene()        { return endScene; }
    public Parent getHowToPlayScene()  { return howToPlayScene; }
    public Parent getPlayerNameScene() { return playerNameScene; }
    public Parent getGameScene()       { return gameScene; }
	public Parent getSettingsScene() { return settingsScene; }
	public Parent getExceptionScene() { return exceptionScene; }


	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
}