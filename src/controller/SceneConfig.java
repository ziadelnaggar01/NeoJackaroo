package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import view.BoardController;

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
    private Parent descriptionScene;

    private boolean inGame = false;
    private BoardController gameController;
    private view.description.Controller descriptionController;

    

    // Private constructor: load all FXML here
    private SceneConfig() {
        try {
            startScene      = FXMLLoader.load(getClass().getResource("/view/startMenu/Scene.fxml"));
            endScene        = FXMLLoader.load(getClass().getResource("/view/endScreen/Scene.fxml"));
            howToPlayScene  = FXMLLoader.load(getClass().getResource("/view/HowToPlayScreen/Scene.fxml"));
            playerNameScene = FXMLLoader.load(getClass().getResource("/view/PlayerName/Scene.fxml"));
            settingsScene   = FXMLLoader.load(getClass().getResource("/view/settingsMenu/Scene.fxml"));
            exceptionScene  = FXMLLoader.load(getClass().getResource("/view/exception/Scene.fxml"));

            
            // Load Game Scene and controller
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/BoardScene.fxml"));
            gameScene = gameLoader.load();
            gameController = gameLoader.getController();
            
            
            //load Description scene and controller
            FXMLLoader descriptionLoader = new FXMLLoader(getClass().getResource("/view/description/Scene.fxml"));
            descriptionScene = descriptionLoader.load();
            descriptionController = descriptionLoader.getController();
            
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
	public Parent getDescriptionScene() { return descriptionScene; }



	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public void setPlayerName(String name) {
		gameController.assignNames(name);
	  }
	
	public view.description.Controller getDescriptionController() {
	    return descriptionController;
	}
}