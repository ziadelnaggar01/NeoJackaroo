package view.HowToPlayScreen;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML private ScrollPane howToPlayScrollPane;

	@FXML private TextFlow instructions;

	@FXML
	private void initialize() {
		instructions.getChildren().clear();
		instructions.setPadding(new Insets(20));

		instructions.getChildren().add(createStyledText("How to Play NeoJackaroo\n\n", "neon-title"));

		instructions.getChildren().add(createStyledText("🎯 Objective:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Move all 4 of your marbles from your Home Zone to your Safe Zone before your 3 CPU opponents do.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("🎲 Game Setup:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("You play against 3 CPU players, each with a unique color.\nEach player has 4 marbles that start in their Home Zone.\nThe game board includes a 100-cell track, plus special zones and traps.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("🃏 Cards & Rounds:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Each round, every player gets 4 cards from a special 102-card deck.\nTurns proceed clockwise, one card per player per turn.\nCards let you move, field, swap, burn, discard, or save marbles.\nUnusable cards are discarded to the Fire Pit, and your turn is skipped.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("🕹️ Your Turn:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Select a card from your hand.\nPerform the associated action (e.g., move a marble X steps).\nStrategically plan around opponents and traps.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("🏁 Marble Movement:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Marbles must be fielded from Home Zone to Base using an Ace or King.\nMarbles move clockwise along the track based on the card’s rank.\nYou can’t pass or destroy your own marbles.\nEntering the Safe Zone requires an exact move count.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("💥 Special Cells:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Trap Cells: Randomly change positions and destroy any marble landing on them.\nBase Cell: Starting cell for marbles; blocks other marbles from passing.\nSafe Zone Entry: Blocks marbles from entering if already occupied.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("💡 Strategic Actions:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Use Jack to swap marbles with opponents.\nUse Burner Wild Card to send an enemy marble back home.\nUse Saver Wild Card to instantly rescue your own marble to a safe cell.\nUse Ten or Queen to discard an opponent’s card and skip their turn.\n\n", "neon-body"));

		instructions.getChildren().add(createStyledText("🏆 Winning:\n", "neon-subtitle"));
		instructions.getChildren().add(createStyledText("Be the first to move all 4 marbles into your Safe Zone. That’s how you win NeoJackaroo!\n", "neon-body"));

		howToPlayScrollPane.getStyleClass().add("scroll-pane");
	}

	
	private Text createStyledText(String content, String styleClass) {
		Text text = new Text(content);
		text.setWrappingWidth(500);
		text.getStyleClass().add(styleClass);
		return text;
	}

	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
}
