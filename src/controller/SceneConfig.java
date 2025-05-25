package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Colour;
import view.BoardController;
import view.endScreen.Controller;

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
	private Parent aboutScene;
	private Parent fullScreenAlert;

	private boolean inGame = false;
	private Parent lastScene = startScene;
	private BoardController gameController;
	private view.endScreen.Controller endScreenController;
	private view.description.Controller descriptionController;
	private view.exception.Controller exceptionController;

	// Private constructor: load all FXML here
	private SceneConfig() {
		try {
			startScene = FXMLLoader.load(getClass().getResource("/view/startMenu/Scene.fxml"));
			howToPlayScene = FXMLLoader.load(getClass().getResource("/view/HowToPlayScreen/Scene.fxml"));
			playerNameScene = FXMLLoader.load(getClass().getResource("/view/PlayerName/Scene.fxml"));
			settingsScene = FXMLLoader.load(getClass().getResource("/view/settingsMenu/Scene.fxml"));
			aboutScene = FXMLLoader.load(getClass().getResource("/view/aboutMenu/Scene.fxml"));
			fullScreenAlert = FXMLLoader.load(getClass().getResource("/view/fullScreenAlertScene.fxml"));

			// Load Game Scene and controller
			FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/BoardScene.fxml"));
			gameScene = gameLoader.load();
			gameController = gameLoader.getController();

			// load Description scene and controller
			FXMLLoader descriptionLoader = new FXMLLoader(getClass().getResource("/view/description/Scene.fxml"));
			descriptionScene = descriptionLoader.load();
			descriptionController = descriptionLoader.getController();

			// load Exception scene and controller
			FXMLLoader exceptionLoader = new FXMLLoader(getClass().getResource("/view/exception/Scene.fxml"));
			exceptionScene = exceptionLoader.load();
			exceptionController = exceptionLoader.getController();
			
			// Load End Screen and controller
			FXMLLoader endScreenLoader = new FXMLLoader(getClass().getResource("/view/endScreen/Scene.fxml"));
			endScene = endScreenLoader.load();
			endScreenController = endScreenLoader.getController();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

	/** Global accessor */
	public static SceneConfig getInstance() {
		return INSTANCE;
	}

	/** Getters for each scene */
	public Parent getStartScene() {
		lastScene = startScene;
		return startScene;
	}

	public Parent getEndScene() {
		lastScene = endScene;
		return endScene;
	}

	public Parent getHowToPlayScene() {
		lastScene = howToPlayScene;
		return howToPlayScene;
	}

	public Parent getPlayerNameScene() {
		lastScene = playerNameScene;
		return playerNameScene;
	}

	public Parent getGameScene() {
		lastScene = gameScene;
		return gameScene;
	}

	public Parent getSettingsScene() {
		lastScene = settingsScene;
		return settingsScene;
	}

	public Parent getExceptionScene() {
		lastScene = exceptionScene;
		return exceptionScene;
	}

	public Parent getDescriptionScene() {
		lastScene = descriptionScene;
		return descriptionScene;
	}
	
	public Parent getAboutScene() {
		lastScene = aboutScene;
		return aboutScene;
	}

	public Parent getFullScreenAlert() {
		return fullScreenAlert;
	}
	
	public Parent getLastScene() {
		return lastScene;
	}


	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public void setPlayerName(String name) {
		gameController.assignNames(name);
		gameController.setCurrentPlayerLabel();
		gameController.setNextPlayerLabel();
	}
	
	public void setWinnerName(Colour winner,Colour userColour)
	{
		endScreenController.updateWinner(winner,userColour);
	}
	
	public void setStatistics(int time, int discards, int traps, int turns)
	{
		endScreenController.updateStatistics(time,discards,traps,turns);
	}
	
	public Controller getEndScreenController(){
		return endScreenController;
	}
	
	
	public void createNewGame()
	{
		try
		{
		FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/BoardScene.fxml"));
		gameScene = gameLoader.load();
		gameController = gameLoader.getController();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public view.description.Controller getDescriptionController() {
		return descriptionController;
	}

	public view.exception.Controller getExceptionController() {
		return exceptionController;
	}
	
	public void enablePlayerButtons(){
		gameController.enablePlayerButtons();
	}

}